package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.screens.components.dummyFaceDetailsList
import com.google.gson.Gson
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody
import org.json.JSONObject


class FaceDetailsViewModel : ViewModel() {

//    private val _faces = MutableLiveData<ResponseBody>()
//    val faces: LiveData<ResponseBody> get() = _faces

    private val _faces = MutableLiveData<List<FaceDetails>>() // ✅ Store parsed list
    val faces: LiveData<List<FaceDetails>> get() = _faces



    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchFaceData(request: RequestData) {
        val call = RetrofitClient.apiService.getFaceDetails(request)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {

                    val rawJson = response.body()?.string()

                    try {
                        val jsonObject = JSONObject(rawJson ?: "")
                        val dataArray = jsonObject.getJSONArray("data")

                        val gson = Gson()

                        val faceList = mutableListOf<FaceDetails>()

                        for (i in 0 until dataArray.length()) {
                            val item = dataArray.getJSONObject(i).toString()
                            val face = gson.fromJson(item, FaceDetails::class.java)
                            faceList.add(face)
                        }



                        Log.d("API", "Data Received :>>> $faceList")
                        // ✅ Post parsed data to LiveData
                        _faces.postValue(faceList)

                    } catch (e: Exception) {
                        Log.e("API", "JSON parsing error: ${e.message}")
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