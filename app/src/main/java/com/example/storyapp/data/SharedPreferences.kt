package com.example.storyapp.data


import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.storyapp.data.dataClass.DataSharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class SharedPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun getUser(): Flow<DataSharedPreferences> {
        return dataStore.data.map {preferences ->
            DataSharedPreferences(
                preferences[NAME_KEY] ?:"",
                preferences[EMAIL_KEY] ?:"",
                preferences[TOKEN_KEY]?:"",
                preferences[STATE_KEY] ?: false
            )

        }
    }

    suspend fun saveUser(user : DataSharedPreferences){
        dataStore.edit { preferences->
            preferences[NAME_KEY] = user.username
            preferences[EMAIL_KEY] = user.userId
            preferences[TOKEN_KEY] = user.token
            preferences[STATE_KEY] = user.isLogin
        }
        val preferencesData = dataStore.data.first()
        val savedUser = DataSharedPreferences(
            preferencesData[NAME_KEY] ?: "",
            preferencesData[EMAIL_KEY] ?: "",
            preferencesData[TOKEN_KEY] ?: "",
            preferencesData[STATE_KEY] ?: false
        )
        val isUserSaved = savedUser == user
        Log.d("SharedPreferences", "User data saved: $isUserSaved")

    }
    suspend fun setToken(Token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = Token
        }
    }
    suspend fun logout(){
        dataStore.edit { preferences->
            preferences.clear()
        }
        val preferencesData = dataStore.data.first()
        val allKeys = preferencesData.asMap().keys
        val isPreferencesCleared = allKeys.isEmpty()
        Log.d("SharedPreferences", "All preferences data cleared: $isPreferencesCleared")

    }


    companion object {
        @Volatile
        private var INSTANCE: SharedPreferences? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val TOKEN_KEY = stringPreferencesKey("token_setting")


        fun instance(dataStore: DataStore<Preferences>): SharedPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SharedPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
