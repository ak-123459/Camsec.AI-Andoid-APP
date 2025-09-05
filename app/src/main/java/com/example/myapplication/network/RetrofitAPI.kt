package com.example.myapplication.network

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface ApiService {

    @POST("login+saveFCMToken.php") // Replace with your endpoint
     fun loginParents(@Body request: LoginRequestData): Call<ResponseBody> // or suspend fun for coroutine

    @POST("get_student_by_parent_code_tenant.php",)
    // Replace with your endpoint
   suspend fun getStudentByParentID(@Body request: GetStudentByParentCode,@Header("Authorization") token: String): Response<ResponseBody> // or suspend fun for coroutine

    @POST("get_attendance_by_stdID_date_tenant.php")
     fun getAttendanceManual(@Body request: AttendanceRequest,@Header("Authorization") token: String): Call<ResponseBody>

    @POST("attendance/monthly.php")
      fun getMonthlyAttendance(@Body request: AttendanceRequest,@Header("Authorization") token: String):  Call<ResponseBody>

    @POST("attendance/weekly.php")
     fun getWeeklyAttendance(@Body request: AttendanceRequest,@Header("Authorization") token: String):  Call<ResponseBody>

}




