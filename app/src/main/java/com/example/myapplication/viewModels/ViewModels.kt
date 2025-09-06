package com.example.myapplication.viewModels

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.network.DemoFaceDetails
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.screens.components.dummyFaceDetailsList
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import org.json.JSONObject
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.local.db.DatabaseProvider
import com.example.myapplication.local.db.NotificationEntity
import com.example.myapplication.local.repository.AttendanceRepository
import com.example.myapplication.network.AttendMonthlySummary
import com.example.myapplication.network.AttendanceMonthly
import com.example.myapplication.network.AttendanceResponse
import com.example.myapplication.network.AttendanceWeekly
import com.example.myapplication.network.GetStudentByParentCode
import com.example.myapplication.network.LoginRequestData
import com.example.myapplication.network.LoginResponseData
import com.example.myapplication.network.QueryDayStats
import com.example.myapplication.network.StudentDetails
import com.google.firebase.auth.ActionCodeSettings
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch



sealed class DashboardUiState {
    data object Loading : DashboardUiState()
    data class Success(val students: List<StudentDetails>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
    data object NoInternet : DashboardUiState()
}

class StudentDetailsViewModel : ViewModel() {


    private val _faces = MutableLiveData<List<StudentDetails>>()
    val faces: LiveData<List<StudentDetails>> get() = _faces

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()



        private val _studentImage = MutableLiveData<Bitmap>()
        val studentImage: LiveData<Bitmap> get() = _studentImage

        private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
        val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

        fun fetchFaceData(request: GetStudentByParentCode, accessToken: String) {
            // Use viewModelScope to launch the coroutine.
            viewModelScope.launch {
                _uiState.value = DashboardUiState.Loading // Set loading state immediately.
                try {
                    // Use the suspend function directly without the enqueue callback.
                    val response =
                        RetrofitClient.apiService.getStudentByParentID(request, accessToken)

                    if (response.isSuccessful) {


                        val rawJson = response.body()?.string() ?: ""
                        val jsonObject = JSONObject(rawJson)
                        val dataArray = jsonObject.getJSONArray("students")

                        Log.d("STUDENT-DETAILS->", "Full Response  $dataArray")

                        val studentList = mutableListOf<StudentDetails>()
                        val gson = Gson()

                        for (i in 0 until dataArray.length()) {
                            val item = dataArray.getJSONObject(i).toString()
                            studentList.add(gson.fromJson(item, StudentDetails::class.java))
                        }

                        if (studentList.isEmpty()) {
                            _uiState.value = DashboardUiState.Error("No students found.")
                        } else {
                            _uiState.value = DashboardUiState.Success(studentList)
                        }
                    } else {
                        _uiState.value = DashboardUiState.Error("Error: ${response.code()}")
                    }
                } catch (e: Exception) {
                    // Handle network failures, JSON parsing errors, etc.
                    _uiState.value =
                        DashboardUiState.Error("Network error: \uD83C\uDF10 try after sometimes..."
                        )
                }
            }
        }

        fun setStudentImage(bitmap: Bitmap) {
            _studentImage.value = bitmap
        }
    }


class DemoFaceDetailsViewModel : ViewModel() {

        private val _faces = MutableLiveData<List<DemoFaceDetails>>() // ✅ Store parsed list
        val faces: LiveData<List<DemoFaceDetails>> get() = _faces


        private val _error = MutableLiveData<String>()
        val error: LiveData<String> get() = _error

        suspend fun getDemoFacesDetails() {


            _faces.postValue(dummyFaceDetailsList)

        }

    }


    class SignInViewModel : ViewModel() {

        private val _email = mutableStateOf("")
        val email: State<String> = _email

        fun onEmailChange(newEmail: String) {
            _email.value = newEmail
        }

        fun sendSignInLink(context: Context, onSendLinkSuccess: () -> Unit) {
            val actionCodeSettings = ActionCodeSettings.newBuilder()
                .setHandleCodeInApp(true)
                .setUrl("https://camsec-ai.firebaseapp.com/__/auth/handler")
                .setAndroidPackageName(
                    context.packageName,
                    true,
                    "1"
                ).build()

            Firebase.auth.sendSignInLinkToEmail(email.value, actionCodeSettings)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Save the email to SharedPreferences
                        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                        prefs.edit { putString("email", email.value) }
                        onSendLinkSuccess()
                    } else {
                        // Handle failure
                    }
                }

        }
    }


    class LoginViewModel : ViewModel() {

        private val repository = AttendanceRepository()
        private val _loginResult = MutableLiveData<LoginResponseData?>()
        val loginResult: LiveData<LoginResponseData?> get() = _loginResult
        private val _isLoading = mutableStateOf(false)
        val isLoading: State<Boolean> = _isLoading


        private val _error = MutableLiveData<String?>()
        val error: LiveData<String?> get() = _error

        // NEW: FCM token send status
        private val _fcmSendSuccess = MutableLiveData<Boolean?>()
        val fcmSendSuccess: LiveData<Boolean?> get() = _fcmSendSuccess

        private val _fcmSendError = MutableLiveData<String?>()
        val fcmSendError: LiveData<String?> get() = _fcmSendError

        fun loginUser(request: LoginRequestData) {
            val call = RetrofitClient.apiService.loginParents(request)
            _isLoading.value = true

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {



                    if (response.isSuccessful) {
                        try {
                            val responseBody = response.body()?.string()
                            val jsonObject = JSONObject(responseBody ?: "")

                            val success = jsonObject.optBoolean("success", false)
                            val message = jsonObject.optString("message", "Please try again...")



                            if (success) {

                                // ✅ Extract access token
                                val accessToken = jsonObject.optString("access_token", "")
                                val userJson = jsonObject.optJSONObject("user")
                                val gson = Gson()
                                val user = gson.fromJson(
                                    userJson!!.toString(),
                                    LoginResponseData::class.java
                                )
                                user.access_token = "Bearer $accessToken"
                                _loginResult.postValue(user)

                                Log.d("LOGIN->", "Full Response  $userJson")

                                request.parent_code?.let { Log.d("PARENT CODE IS :-> ", it) }
                                // ✅ Send FCM token after login success

                            } else {

                                Log.e("LoginViewModel", "----->> error: ${response.body()}")

                                val message = jsonObject.optString("message", "Login failed")
                                _error.postValue(message)
                            }

                        } catch (e: Exception) {

                            Log.e("LoginViewModel", "JSON parsing error: ${e.message}")
                            _error.postValue("Parsing error: ${e.message}")


                        } finally {

                            _isLoading.value = false
                        }
                    } else {
                        _isLoading.value = false
                        if(response.code()==401){

                            _error.postValue("Login failed: Invalid Parent Code or Password...")

                        }
                        Log.e("LoginViewModel", "--+++---++>>>>>>: ${response.body()}")
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _error.postValue("\uD83C\uDF10 try after sometimes... ")
                    _isLoading.value = false
                }
            })
        }

    }
//
//    // ✅ New: Send FCM token
//    private fun sendFCMToken(parentCode: String, token: String,accessToken:String) {
//
//        val request = SendFcmToken(parentCode, token)
//        val call = RetrofitClient.apiService.sendFCMToken(request,"Bearer $accessToken")
//
//
//        call.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//
//                if (response.isSuccessful) {
//
//                    val responseBody = response.body()?.string()
//                    val jsonObject = JSONObject(responseBody ?: "")
//
//                    val success = jsonObject.optBoolean("success", false)
//
//                    if(success){
//
//                        _fcmSendSuccess.postValue(true)
//                        _fcmSendError.postValue("FCM success: ${response.code()}")
//                        Log.d("FCM->", "FCM success: ${token}")
//
//
//                    }else {
//
//                        val message = jsonObject.optString("message", "FCM Token Send failed")
//                        _fcmSendError.postValue(message)
//                        Log.d("FCM--7673737373->", "FCM success: ${token}")
//
//
//
//                    }
//
//
//                } else {
//                    _fcmSendSuccess.postValue(false)
//                    _fcmSendError.postValue("FCM failed: ${response.code()}")
//                    Log.d("FCM->", "FCM Send Failed: ${response.body()}")
//
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                _fcmSendSuccess.postValue(false)
//                _fcmSendError.postValue("FCM error: ${t.message}")
//                Log.d("FCM---------------------------------FCM>", "FCM send failed: ${t.message}")
//
//            }
//        })
//    }



    class AttendanceViewModel : ViewModel() {

        private val repository = AttendanceRepository()

        private val _attendance = MutableLiveData<AttendanceResponse?>(null)
        val attendance: LiveData<AttendanceResponse?> = _attendance


        private val _isLoading = mutableStateOf(false)
        val isLoading: State<Boolean> = _isLoading

        private val _errorMessage = MutableLiveData<String?>(null)
        val errorMessage: LiveData<String?> = _errorMessage


        private val _weeklyStats = MutableLiveData<List<AttendanceWeekly?>?>()
        val weeklyStats: LiveData<List<AttendanceWeekly?>?> = _weeklyStats

        private val _queryDayStats = MutableLiveData<QueryDayStats?>()
        val queryDayStats: LiveData<QueryDayStats?> = _queryDayStats

        private val _monthlySummary = MutableLiveData<AttendMonthlySummary?>(null)

        val monthlySummary: LiveData<AttendMonthlySummary?> = _monthlySummary

        fun fetchAttendance(stdId: Int, date: String, accessToken: String) {
            _isLoading.value = true
            _errorMessage.value = null
            _attendance.value = null   // reset previous data


            repository.getAttendanceManual(
                stdId = stdId,
                date = date,
                accessToken = accessToken,
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {

                        if (response.isSuccessful) {

                            try {
                                val responseBody = response.body()?.string()
                                val jsonObject = JSONObject(responseBody ?: "")

                                val success = jsonObject.optBoolean("success", false)

                                if (success) {
                                    val attendanceJson = jsonObject.optJSONObject("attendance")
                                    val gson = Gson()
                                    val attendanceObj = gson.fromJson(
                                        attendanceJson!!.toString(),
                                        AttendanceResponse::class.java
                                    )
                                    _attendance.value = attendanceObj.copy()
                                    Log.d("ATTENDANCE_VIEWMODEL", "Parsed: $attendanceObj")
                                } else {
                                    _errorMessage.value =
                                        jsonObject.optString("message", "Attendance fetch failed")
                                }

                            } catch (e: Exception) {
                                _errorMessage.value = "Parsing error: ${e.message}"
                                Log.e("ATTENDANCE_VIEWMODEL", "Parsing error: ${e.message}")
                            } finally {
                                _isLoading.value = false
                            }


                        } else {
                            _isLoading.value = false
                            _errorMessage.value = "Server error: ${response.code()}"
                            Log.e("ATTENDANCE_VIEWMODEL", "Parsing error: ${response.body()}")

                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        _isLoading.value = false
                        _errorMessage.value = "Network error: \uD83C\uDF10 try after sometimes..."
                        Log.e("ATTENDANCE_VIEWMODEL", "Failure: ${t.message}")
                    }
                })
        }

        fun fetchAttendancesStats(stdId: Int, date: String, accessToken: String) {
            _isLoading.value = true
            _errorMessage.value = null
            _weeklyStats.value = null   // reset
            _queryDayStats.value = null   // reset
            _monthlySummary.value = null
            _attendance.value = null

            repository.fetchAttendStats(
                stdId = stdId,
                date = date,
                accessToken = accessToken,
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            try {
                                val responseBody = response.body()?.string()
                                val jsonObject = JSONObject(responseBody ?: "")
                                val success = jsonObject.optBoolean("success", false)

                                if (success) {
                                    val gson = Gson()

                                    // 1) Parse Query Day
                                    val queryDayObj = jsonObject.optJSONObject("query_day")
                                    queryDayObj?.let {
                                        val queryDay = gson.fromJson(it.toString(), QueryDayStats::class.java)
                                        _queryDayStats.value = queryDay
                                    }

                                    // 2) Parse Weekly Attendance
                                    val weeklyArray = jsonObject.optJSONArray("weekly_attendance")
                                    if (weeklyArray != null) {
                                        val weeklyList = mutableListOf<AttendanceWeekly>()
                                        for (i in 0 until weeklyArray.length()) {
                                            val item = weeklyArray.getJSONObject(i)
                                            weeklyList.add(
                                                AttendanceWeekly(
                                                    date = item.optString("date"),
                                                    day_name = item.optString("day_name"),
                                                    status = when (item.optString("status")) {
                                                        "Present" -> true
                                                        "Absent" -> false
                                                        else -> false
                                                    }
                                                )
                                            )
                                        }
                                        _weeklyStats.value = weeklyList
                                    }

                                    // 3) Parse Monthly Summary
                                    val monthlyObj = jsonObject.optJSONObject("monthly_summary")
                                    monthlyObj?.let {
                                        val monthlySummary = gson.fromJson(
                                            it.toString(),
                                            AttendMonthlySummary::class.java
                                        )
                                        _monthlySummary.value = monthlySummary
                                    }

                                    Log.d("ATTENDANCE_VIEWMODEL", "Parsed weekly + monthly stats")

                                } else {
                                    _errorMessage.value =
                                        jsonObject.optString("message", "Attendance fetch failed")
                                }

                            } catch (e: Exception) {
                                _errorMessage.value = "Parsing error: ${e.message}"
                                Log.e("ATTENDANCE_VIEWMODEL", "Parsing error", e)
                            } finally {
                                _isLoading.value = false
                            }
                        } else {
                            _isLoading.value = false
                            _errorMessage.value = "Server error: ${response.code()}"
                            Log.e("ATTENDANCE_VIEWMODEL", "Server error: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        _isLoading.value = false
                        _errorMessage.value = "Network error: 🌐 try again later..."
                        Log.e("ATTENDANCE_VIEWMODEL", "Failure: ${t.message}")
                    }
                })
        }



    }


    class NotificationViewModel(application: Application) : AndroidViewModel(application) {

        private val dao = DatabaseProvider.getDatabase(application).notificationDao()

        // MutableStateFlow to hold and update the notification state
        private val _hasNewNotifications = MutableStateFlow(false)

        // Expose as an immutable StateFlow for the UI to observe
        val hasNewNotifications: StateFlow<Boolean> = _hasNewNotifications.asStateFlow()

        val notifications: StateFlow<List<NotificationEntity>> =
            dao.getAllNotifications()
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // 🔹 New: State to track selected notification IDs for multi-selection
        var selectedNotificationIds = mutableStateOf(emptySet<Int>())
            private set

        // 🔹 New: Toggles the selection of a single notification.
        fun toggleNotificationSelection(notificationId: Int) {
            selectedNotificationIds.value =
                if (selectedNotificationIds.value.contains(notificationId)) {
                    selectedNotificationIds.value - notificationId
                } else {
                    selectedNotificationIds.value + notificationId
                }
        }

        // 🔹 New: Toggles between selecting all and clearing all selections.
        fun toggleSelectAllNotifications() {
            if (selectedNotificationIds.value.size < notifications.value.size) {
                // Select all if not all are currently selected
                selectedNotificationIds.value = notifications.value.map { it.id }.toSet()
            } else {
                // Otherwise, clear all selections
                selectedNotificationIds.value = emptySet()
            }
        }

        // 🔹 New: Deletes all notifications currently in the selectedNotificationIds set.
        fun deleteSelectedNotifications() {
            val selectedIds = selectedNotificationIds.value
            viewModelScope.launch(Dispatchers.IO) {
                selectedIds.forEach { id ->
                    val notificationToDelete = dao.getById(id)
                    notificationToDelete?.let {
                        dao.delete(it)
                    }
                }
                // Clear the selection after deletion
                selectedNotificationIds.value = emptySet()
            }
        }

        fun markAsRead(id: Int) {
            viewModelScope.launch(Dispatchers.IO) {
                val notif = dao.getById(id)?.copy(isRead = true)
                notif?.let { dao.update(it) }
            }
        }

        fun clearNotificationStatus() {
            _hasNewNotifications.value = false
        }

        // 🔹 Function to log all notifications
        fun logAllNotifications() {
            viewModelScope.launch(Dispatchers.IO) {
                dao.getAllNotifications().collect { list ->
                    list.forEach { notif ->
                        Log.d(
                            "RoomDebug",
                            "Notification: id=${notif.id}, title=${notif.title}, body=${notif.body}, isRead=${notif.isRead}, timestamp=${notif.timestamp}"
                        )
                    }
                    _hasNewNotifications.value = true
                }
            }
        }

        // 🔹 Function to delete a notification using the entity
        fun deleteNotification(id: Int) {
            viewModelScope.launch(Dispatchers.IO) {
                val notificationToDelete = dao.getById(id)
                notificationToDelete?.let {
                    dao.delete(it)
                }
            }
        }
    }

    class NotificationViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NotificationViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }








