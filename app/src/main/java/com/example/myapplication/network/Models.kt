package com.example.myapplication.network




data class StudentDetails(

    val id:Int?,
    val full_name: String?,
    val email:String?,
    val class_id:String?,
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
    val parent_code:String?,
    val password_hash:String?,
    val fcm_token: String?
)


data class LoginResponseData(
    val id:String?,
    val full_name:String?,
    val email:String?,
    val phone_number:String?,
    var access_token: String? = null

)


data class GetStudentByParentCode(
    val parent_code: String?
)



data class GetAttendance(
    val std_id: String?,
    val date:String
)


// Attendance.kt
data class AttendanceRequest(
    val std_id: Int?,
    val date: String?
)


data class AttendanceResponse(
    val present: Int?,
    val created_at:String?,
    val detected_image:String?
)



data class ParentsProfile(

    val full_name:String?,
    val email:String?,
    val phone_number:String?

)


data class  SendFcmToken(
    val  parent_code: String,var fcm_token:String
)


