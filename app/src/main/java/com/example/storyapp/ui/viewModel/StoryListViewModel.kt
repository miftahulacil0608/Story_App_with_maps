package com.example.storyapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.dataClass.StoryItem
import com.example.storyapp.repository.StoryListRepository

class StoryListViewModel(private val storyListRepository : StoryListRepository): ViewModel() {
    val story : LiveData<PagingData<StoryItem>> =
        storyListRepository.getAllStory().cachedIn(viewModelScope)


}