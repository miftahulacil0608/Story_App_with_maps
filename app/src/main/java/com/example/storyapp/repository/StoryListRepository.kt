package com.example.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.QuotePagingSource
import com.example.storyapp.data.dataClass.StoryItem
import com.example.storyapp.data.remote.ApiService

class StoryListRepository(private val apiService : ApiService) {
    fun getAllStory():LiveData<PagingData<StoryItem>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                QuotePagingSource(apiService)
            }
        ).liveData
    }
}