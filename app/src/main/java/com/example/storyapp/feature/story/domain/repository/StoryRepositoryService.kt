package com.example.storyapp.feature.story.domain.repository

import com.example.storyapp.feature.story.domain.model.AddStoryResponse
import com.example.storyapp.feature.story.domain.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface StoryRepositoryService {

    suspend fun getStory(token: String, page: Int?, size: Int?): StoryResponse
    suspend fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddStoryResponse
}