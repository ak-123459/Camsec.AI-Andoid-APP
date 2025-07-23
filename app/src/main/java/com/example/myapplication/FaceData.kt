package com.example.myapplication




data class FaceDetails(

    val id:Int,
    val student_id:Int,
    val student_name: String,
    val camera_location:String,
    val camera_name:String,
    val cropped_face:String,
    val date:String,
    val registration_face:String,
    val original_face:String,
    val recognition_time:String
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





data class RequestData(
    val date:String
    )
