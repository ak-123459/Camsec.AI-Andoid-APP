package com.example.myapplication.local.repository

import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import com.example.myapplication.network.AttendanceRequest
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.network.SendFcmToken
import retrofit2.Callback


fun decodeBase64ToBitmap(base64Str: String): Bitmap {
    val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}



fun bitmapToImageBitmap(bitmap: Bitmap): ImageBitmap {
    return bitmap.asImageBitmap()
}


class AttendanceRepository {
    fun getAttendanceManual(stdId: Int, date: String, callback: Callback<okhttp3.ResponseBody>) {
        val request = AttendanceRequest(std_id = stdId, date = date)
        val call = RetrofitClient.apiService.getAttendanceManual(request)
        call.enqueue(callback)
    }


    fun sendFCMToken(email: String, fcm_token: String, callback: Callback<okhttp3.ResponseBody>) {
        val request = SendFcmToken(email = email, fcm_token = fcm_token)
        val call = RetrofitClient.apiService.sendFCMToken(request)
        call.enqueue(callback)
    }


}








