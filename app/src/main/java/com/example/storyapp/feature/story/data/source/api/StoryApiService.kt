package com.example.storyapp.feature.story.data.source.api

import com.example.storyapp.feature.story.domain.model.StoryResponse
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface StoryApiService {
    @POST("stories")
    suspend fun getStory(
        @Header("Authorization") token: String
    ): StoryResponse

}