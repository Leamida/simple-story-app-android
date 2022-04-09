package com.example.storyapp.feature.story.data.source.api

import com.example.storyapp.feature.story.domain.model.StoryResponse
import retrofit2.http.*

interface StoryApiService {
    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page:Int?,
        @Query("size") size:Int?
    ): StoryResponse

}