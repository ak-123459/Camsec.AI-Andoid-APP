package com.example.myapplication.network




data class StudentDetails(

    val std_id:Int?,
    val full_name: String?,
    val email:String?,
    val roll_no:String?,
    val class_id:String?,
    val created_at:String?,
    val image:String?,
)




data class DemoFaceDetails(

    val id:Int,
    val student_id:Int,
    val student_name: String,
    val camera_location:String,
    val camera_name:String,
    val cropped_face:Int,
    val date:String,
    val registration_face:Int,
    val original_face:Int,
    val recognition_time:String
)








data class LoginRequestData(
    val email:String,
    val password_hash:String
)


data class LoginResponseData(
    val id:Int?,
    val full_name:String?,
    val email:String?,
    val phone_number:String?,
    val device_token:String?
)


data class GetStudentByEmail(
    val email: String?
)



data class GetAttendance(
    val std_id: String,
    val date:String
)


// Attendance.kt
data class AttendanceRequest(
    val std_id: Int,
    val date: String
)


data class AttendanceResponse(
    val std_id: Int,
    val present: Int,
    val date: String,          // keep as String unless you want Date parsing
    val class_id: String?,
    val created_at:String?
)


data class ParentsProfile(

    val full_name:String?,
    val email:String?,
    val phone_number:String?

)


data class  SendFcmToken(
    val  email: String,var fcm_token:String
)


