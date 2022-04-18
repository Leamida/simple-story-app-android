package com.example.storyapp.presentation.view_model

import androidx.lifecycle.ViewModel
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

    fun getStories(token: String, page: Int? = null, size: Int? = null) =
        getStoriesUseCase(token, page, size)

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) =
        addStoryUseCase(token, file, description,lat, lon)
}