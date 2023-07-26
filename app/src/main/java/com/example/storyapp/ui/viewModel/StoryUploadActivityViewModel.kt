package com.example.storyapp.ui.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.dataClass.FileUploadResponse
import com.example.storyapp.data.remote.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryUploadActivityViewModel: ViewModel() {
    private var _isSuccess : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var isSuccess : LiveData<Boolean> = _isSuccess
    private var _isLoading : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var isLoading : LiveData<Boolean> = _isLoading

    fun uploadStory(context : Context,file : MultipartBody.Part,description: RequestBody){
        _isLoading.value = true
        val service = ApiConfig.getApiService(context).uploadStory(file,description)
        service.enqueue(object : Callback<FileUploadResponse>{
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {

                if (response.isSuccessful){
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody!=null && !responseBody.error){
                        _isSuccess.value = true
                        Toast.makeText(context, responseBody.message, Toast.LENGTH_SHORT).show()
                    }
                }else{
                    _isSuccess.value = false
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}