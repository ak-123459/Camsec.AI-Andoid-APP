package com.example.myapplication.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.myapplication.App















object RetrofitClient {

    private const val BASE_URL = "https://camsecai.com/api/tenants_ats/" // include trailing slash

//    private val cacheSize = 10L * 1024 * 1024 // 10 MB
//    private val cache by lazy { Cache(App.context.cacheDir, cacheSize) } // Use your Application context
//
//    private val okHttpClient by lazy {
//        OkHttpClient.Builder()
//            .cache(cache)
//            .addInterceptor { chain ->
//                var request = chain.request()
//                request = if (hasNetwork(App.context)) {
//                    // Online: cache responses for 5 seconds
//                    request.newBuilder().header("Cache-Control", "public, max-age=60").build()
//                } else {
//                    // Offline: cache up to 7 days
//                    request.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=600")
//                        .build()
//                }
//                chain.proceed(request)
//            }
//            .build()
//    }





    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
