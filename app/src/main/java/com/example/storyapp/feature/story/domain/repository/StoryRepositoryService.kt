package com.example.storyapp.feature.story.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.storyapp.feature.story.domain.model.AddStoryResponse
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.feature.story.domain.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepositoryService {

    fun getStory(token: String): LiveData<PagingData<ListStoryItem>>
    suspend fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddStoryResponse
}