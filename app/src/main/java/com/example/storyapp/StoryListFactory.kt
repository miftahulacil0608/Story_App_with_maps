package com.example.storyapp.paging3dulu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.repository.StoryListRepository
import com.example.storyapp.ui.viewModel.StoryListViewModel

@Suppress("UNCHECKED_CAST")
class StoryListViewModelFactory(private val storyListRepository: StoryListRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryListViewModel::class.java)){
            return StoryListViewModel(storyListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class : "+modelClass.name)
    }
}