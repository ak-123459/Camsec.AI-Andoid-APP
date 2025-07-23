package com.example.myapplication

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import com.example.myapplication.FaceDetails
import okhttp3.ResponseBody

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface ApiService {
    @POST("/events/get_devotee_summary") // Replace with your endpoint
    fun getFaceDetails(@Body request:RequestData ): Call<ResponseBody> // or suspend fun for coroutine
}



object RetrofitClient {

    private const val BASE_URL = "http://192.168.1.13:7000" // include trailing slash

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

