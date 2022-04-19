package com.example.storyapp.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.core.util.Result
import com.example.storyapp.feature.story.data.repository.StoryRepository
import com.example.storyapp.feature.story.domain.model.AddStoryResponse
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.feature.story.domain.model.StoryResponse
import com.example.storyapp.feature.story.domain.use_case.AddStoryUseCase
import com.example.storyapp.feature.story.domain.use_case.GetStoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val getStoriesUseCase: GetStoriesUseCase,
    private val addStoryUseCase: AddStoryUseCase
) : ViewModel() {

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> =
        getStoriesUseCase(token).cachedIn(viewModelScope)

    fun getStories(token: String,location:Int) : LiveData<Result<List<ListStoryItem?>?>> = getStoriesUseCase(token, location)

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?
    ): LiveData<Result<AddStoryResponse?>?> = addStoryUseCase(token, file, description, lat, lon)

}