package com.example.storyapp.ui.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.dataClass.SignupResponse
import com.example.storyapp.data.remote.ApiConfig

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupViewModel: ViewModel() {
    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signup(context:Context,name:String, email:String, password:String){
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).signup(name = name,email= email, password = password)
        client.enqueue(object : Callback<SignupResponse>{
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                    //Log.d("SignupActivity", "onResponseSignupActivity: ${response.message()}")
                    _isSuccess.postValue(true)
                }else{
                    _isSuccess.postValue(false) 
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                _isLoading.value = true
                Log.d("Failure", "${t.message}")
            }

        })
    }
}