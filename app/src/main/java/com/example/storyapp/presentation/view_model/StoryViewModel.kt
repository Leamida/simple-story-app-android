package com.example.storyapp.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.feature.story.data.repository.StoryRepository
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.feature.story.domain.use_case.AddStoryUseCase
import com.example.storyapp.feature.story.domain.use_case.GetStoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val getStoriesUseCase: GetStoriesUseCase,
    private val addStoryUseCase: AddStoryUseCase,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getStories(token: String) : LiveData<PagingData<ListStoryItem>>{
        return storyRepository.getStory(token).cachedIn(viewModelScope)
    }

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) =
        addStoryUseCase(token, file, description, lat, lon)
}