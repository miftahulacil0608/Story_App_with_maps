package com.example.storyapp.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.storyapp.data.dataClass.LoginResponse
import com.example.storyapp.data.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.storyapp.data.SharedPreferences
import com.example.storyapp.data.dataClass.DataSharedPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val preferences: SharedPreferences) : ViewModel() {
    private var _user = MutableLiveData<LoginResponse>()
    val user: LiveData<LoginResponse> = _user
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setToken(token : String){
        viewModelScope.launch {
            preferences.setToken(token)
        }
    }


    fun saveUser(user : DataSharedPreferences){
        viewModelScope.launch {
            preferences.saveUser(user)
        }
    }



    fun login(context : Context, username : String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService(context).login(username,password)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _user.postValue(response.body())
                    Log.d("Hasil dari fetching Login ", response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("onFailure LoginViewModel", "${t.message}")
            }

        })
    }
}