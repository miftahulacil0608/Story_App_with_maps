package com.example.storyapp.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.dataClass.StoryItem
import com.example.storyapp.data.dataClass.StoryResponse
import com.example.storyapp.data.remote.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivityViewModel : ViewModel() {
    private var _userStory = MutableLiveData<ArrayList<StoryItem>>()
    val userStory: LiveData<ArrayList<StoryItem>> = _userStory

    fun getListMaps(context: Context) {

        val client = ApiConfig.getApiService(context).getAllMaps(200, 1)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {

                if (response.isSuccessful) {
                    _userStory.postValue(response.body()?.listStory)
                    //Log.d("MainActivity", "onResponse: ${response.body()?.listStory}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {

                Log.d("MainActivity", "onFailure: ${t.message}")
            }

        })
    }
}