package com.example.storyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.SharedPreferences
import com.example.storyapp.ui.viewModel.LoginViewModel
import com.example.storyapp.ui.viewModel.MainActivityStoryAppViewModel

class ViewModelFactory(private val preferences: SharedPreferences) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(preferences) as T
        }
        if (modelClass.isAssignableFrom(MainActivityStoryAppViewModel::class.java)) {
            return MainActivityStoryAppViewModel(preferences) as T
        }
        throw IllegalArgumentException(modelClass.name)
    }
}