package com.example.myapplication.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.http.Headers
import com.example.myapplication.BuildConfig



interface ApiService {
    @Headers("api_key: DIT-APPpnqupjT2JuCE7pJKQzlFku6hiIvIMpFfNpJ4anaDkAsIQTI3Oghw2YHyz9XJA693")
    @POST("/api/login.php") // Replace with your endpoint
    fun loginParents(@Body request: LoginRequestData): Call<ResponseBody> // or suspend fun for coroutine

    @Headers("api_key: DIT-APPpnqupjT2JuCE7pJKQzlFku6hiIvIMpFfNpJ4anaDkAsIQTI3Oghw2YHyz9XJA693")
    @POST("/api/get_student_by_email.php") // Replace with your endpoint
    fun getStudentByEmail(@Body request: GetStudentByEmail): Call<ResponseBody> // or suspend fun for coroutine

    @Headers("api_key: DIT-APPpnqupjT2JuCE7pJKQzlFku6hiIvIMpFfNpJ4anaDkAsIQTI3Oghw2YHyz9XJA693")
    @POST("/api/get_attendance_by_std_id_and_date.php")
    fun getAttendanceManual(@Body request: AttendanceRequest): Call<ResponseBody>

    @Headers("api_key: DIT-APPpnqupjT2JuCE7pJKQzlFku6hiIvIMpFfNpJ4anaDkAsIQTI3Oghw2YHyz9XJA693")
    @POST("/api/save_fcm_token.php")
    fun sendFCMToken(@Body request: SendFcmToken): Call<ResponseBody>


}



