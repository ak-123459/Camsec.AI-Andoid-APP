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
import com.example.myapplication.network.AttendanceResponse
import com.example.myapplication.network.GetStudentByEmail
import com.example.myapplication.network.LoginRequestData
import com.example.myapplication.network.LoginResponseData
import com.example.myapplication.network.SendFcmToken
import com.example.myapplication.network.StudentDetails
import com.google.firebase.auth.ActionCodeSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class StudentDetailsViewModel : ViewModel() {

    private val _studentImage = MutableLiveData<Bitmap>()
    val studentImage: LiveData<Bitmap> get() = _studentImage
    private val _faces = MutableLiveData<List<StudentDetails>>()
    val faces: LiveData<List<StudentDetails>> get() = _faces

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    suspend fun fetchFaceData(request: GetStudentByEmail) {
        val call = RetrofitClient.apiService.getStudentByEmail(request)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val rawJson = response.body()?.string()
                    Log.d("STUDENTS-DATA", "Data Received: {${rawJson}}")


                    try {
                        val jsonObject = JSONObject(rawJson ?: "")
                        val dataArray = jsonObject.getJSONArray("students") // ✅ Correct key

                        val gson = Gson()
                        val studentList = mutableListOf<StudentDetails>()

                        for (i in 0 until dataArray.length()) {
                            val item = dataArray.getJSONObject(i).toString()
                            val face = gson.fromJson(item, StudentDetails::class.java)
                            studentList.add(face)
                        }

                        Log.d("API-GET-STUDENTS", "Data Received: $studentList")
                        _faces.postValue(studentList)

                    } catch (e: Exception) {
                        Log.e("API", "JSON parsing error: ${e.message}")
                        Log.e("API-DATA", "Raw JSON: ${response.errorBody().toString()}")
                        _error.postValue("Parsing error: ${e.message}")
                    }

                } else {

                    _error.postValue("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _error.postValue("Failure: ${t.message}")
            }
        })
    }

    fun setStudentImage(bitmap: Bitmap) {
        _studentImage.value = bitmap
    }
}



class DemoFaceDetailsViewModel:ViewModel(){

    private val _faces = MutableLiveData<List<DemoFaceDetails>>() // ✅ Store parsed list
    val faces: LiveData<List<DemoFaceDetails>> get() = _faces


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

   suspend fun getDemoFacesDetails(){


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

    fun loginUser(request: LoginRequestData,fcmToken:String) {
        val call = RetrofitClient.apiService.loginParents(request)
        _isLoading.value = true

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    try {
                        val responseBody = response.body()?.string()
                        val jsonObject = JSONObject(responseBody ?: "")

                        val success = jsonObject.optBoolean("success", false)

                        if (success) {


                            val userJson = jsonObject.optJSONObject("user")
                            val gson = Gson()
                            val user = gson.fromJson(userJson.toString(), LoginResponseData::class.java)
                            _loginResult.postValue(user)

                            Log.d("LOGIN->", " ${response.message()}")


                            // ✅ Send FCM token after login success
                            user.email?.let { sendFCMToken(it, fcmToken) }
                        } else {
                            val message = jsonObject.optString("message", "Login failed")
                            _error.postValue(message)
                        }

                    } catch (e: Exception) {
                        Log.e("LoginViewModel", "JSON parsing error: ${e.message}")
                        _error.postValue("Parsing error: ${e.message}")


                    }finally {


                        _isLoading.value = false
                    }
                } else {
                    _isLoading.value = false
                    _error.postValue("Login failed: ${response.code()}")
                }

            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _error.postValue("Network error: ${t.message}")
                _isLoading.value = false
            }
        })
    }




    // ✅ New: Send FCM token
    private fun sendFCMToken(email: String, token: String) {
        val request = SendFcmToken(email, token)
        val call = RetrofitClient.apiService.sendFCMToken(request)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    val responseBody = response.body()?.string()
                    val jsonObject = JSONObject(responseBody ?: "")

                    val success = jsonObject.optBoolean("success", false)

                    if(success){

                        _fcmSendSuccess.postValue(true)
                        _fcmSendError.postValue("FCM success: ${response.code()}")
                        Log.d("FCM->", "FCM success: ${token}")

                    }else {

                        val message = jsonObject.optString("message", "FCM Token Send failed")
                        _fcmSendError.postValue(message)

                    }


                } else {
                    _fcmSendSuccess.postValue(false)
                    _fcmSendError.postValue("FCM failed: ${response.code()}")
                    Log.d("FCM->", "FCM Send Failed: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _fcmSendSuccess.postValue(false)
                _fcmSendError.postValue("FCM error: ${t.message}")
                Log.d("FCM->", "FCM send failed: ${t.message}")

            }
        })
    }
}



class AttendanceViewModel : ViewModel() {

    private val repository = AttendanceRepository()

    private val _attendance = MutableLiveData<AttendanceResponse?>(null)
    val attendance: LiveData<AttendanceResponse?> = _attendance

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchAttendance(stdId: Int, date: String) {
        _isLoading.value = true
        _errorMessage.value = null

        repository.getAttendanceManual(stdId, date, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    try {
                        val responseBody = response.body()?.string()
                        val jsonObject = JSONObject(responseBody ?: "")

                        val success = jsonObject.optBoolean("success", false)
                        if (success) {
                            val attendanceJson = jsonObject.optJSONObject("attendance")
                            val gson = Gson()
                            val attendanceObj = gson.fromJson(attendanceJson.toString(), AttendanceResponse::class.java)
                            _attendance.value = attendanceObj.copy()
                            Log.d("ATTENDANCE_VIEWMODEL", "Parsed: $attendanceObj")
                        } else {
                            _errorMessage.value = jsonObject.optString("message", "Attendance fetch failed")
                        }

                    } catch (e: Exception) {
                        _errorMessage.value = "Parsing error: ${e.message}"
                        Log.e("ATTENDANCE_VIEWMODEL", "Parsing error: ${e.message}")
                    }

                    finally {
                        _isLoading.value = false
                    }
                } else {
                    _isLoading.value = false
                    _errorMessage.value = "Server error: ${response.code()}"
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Network error: ${t.message}"
                Log.e("ATTENDANCE_VIEWMODEL", "Failure: ${t.message}")
            }
        })
    }
}


class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = DatabaseProvider.getDatabase(application).notificationDao()

    val notifications: StateFlow<List<NotificationEntity>> =
        dao.getAllNotifications()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun markAsRead(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val notif = dao.getById(id)?.copy(isRead = true)
            notif?.let { dao.update(it) }
        }
    }

    // 🔹 Function to log all notifications
    fun logAllNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            dao.getAllNotifications().collect { list ->
                list.forEach { notif ->
                    Log.d("RoomDebug", "Notification: id=${notif.id}, title=${notif.title}, body=${notif.body}, isRead=${notif.isRead}, timestamp=${notif.timestamp}")
                }
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






