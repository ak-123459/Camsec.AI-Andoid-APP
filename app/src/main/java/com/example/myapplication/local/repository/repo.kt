package com.example.myapplication.local.repository

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import com.example.myapplication.App.Companion.context
import com.example.myapplication.network.AttendanceRequest
import com.example.myapplication.network.RetrofitClient
import com.example.myapplication.network.SendFcmToken
import retrofit2.Callback

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.myapplication.network.AttendanceMonthly
import com.example.myapplication.network.AttendanceWeekly
import com.google.gson.Gson

fun decodeBase64ToBitmap(base64Str: String?): Bitmap? {
    return try {
        if (base64Str.isNullOrEmpty()) {
            Log.e("decodeBase64ToBitmap", "Input string is null or empty")
            null
        } else {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            if (bitmap == null) {
                Log.e("decodeBase64ToBitmap", "Failed to decode bitmap from Base64")
            }
            bitmap
        }

    } catch (e: IllegalArgumentException) {
        Log.e("decodeBase64ToBitmap", "Invalid Base64 string", e)
        null
    } catch (e: Exception) {
        Log.e("decodeBase64ToBitmap", "Unexpected error", e)
        null
    }
}






fun bitmapToImageBitmap(bitmap: Bitmap): ImageBitmap {
    return bitmap.asImageBitmap()
}


class AttendanceRepository {

    fun getAttendanceManual(stdId: Int, date: String,accessToken:String, callback: Callback<okhttp3.ResponseBody>) {
        val request = AttendanceRequest(std_id = stdId, date = date)
        val call = RetrofitClient.apiService.getAttendanceManual(request,accessToken)
        return call.enqueue(callback)
    }



     fun fetchMonthly(stdId: Int, date: String, accessToken: String,callback: Callback<okhttp3.ResponseBody>){
        val request = AttendanceRequest(std_id = stdId, date = date)

        val response = RetrofitClient.apiService.getMonthlyAttendance(request, accessToken)

        return response.enqueue(callback)

    }


    fun fetchWeekly(stdId: Int, date: String, accessToken: String,callback: Callback<okhttp3.ResponseBody>) {
        val request = AttendanceRequest(std_id = stdId, date = date)
        val response = RetrofitClient.apiService.getWeeklyAttendance(request, accessToken)

       return response.enqueue(callback)
    }





//    fun sendFCMToken(email: String, fcm_token: String, callback: Callback<okhttp3.ResponseBody>) {
//        val request = SendFcmToken(email = email, fcm_token = fcm_token)
//        val call = RetrofitClient.apiService.sendFCMToken(request)
//        call.enqueue(callback)
//    }


}


fun cancelAllNotifications(context: Context) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()
}



fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
}





