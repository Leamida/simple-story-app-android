package com.example.storyapp.feature.story.data.source.api

import com.example.storyapp.feature.story.domain.model.AddStoryResponse
import com.example.storyapp.feature.story.domain.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoryApiService {
    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): AddStoryResponse

}