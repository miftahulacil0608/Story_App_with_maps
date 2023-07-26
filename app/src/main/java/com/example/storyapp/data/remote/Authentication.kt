package com.example.storyapp.data.remote


import android.content.Context
import com.example.storyapp.data.SharedPreferences
import com.example.storyapp.ui.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


class Authentication(context: Context) : Interceptor {
    private val sharedPreference = SharedPreferences.instance(context.dataStore)
    private var authToken = runBlocking {
        sharedPreference.getToken().first()
    }


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .apply {
                addHeader("Authorization", "Bearer $authToken")
                addHeader("Content-Type" ,"multipart/form-data")
            }
            .build()
        return chain.proceed(request)
    }


}