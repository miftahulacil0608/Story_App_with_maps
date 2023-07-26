package com.example.storyapp.ui.viewModel


import androidx.lifecycle.*
import com.example.storyapp.data.SharedPreferences
import com.example.storyapp.data.dataClass.DataSharedPreferences
import kotlinx.coroutines.launch


class MainActivityStoryAppViewModel(private val preferences: SharedPreferences) : ViewModel() {
    fun getUser() : LiveData<DataSharedPreferences> {
        return preferences.getUser().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            preferences.logout()
        }
    }
}