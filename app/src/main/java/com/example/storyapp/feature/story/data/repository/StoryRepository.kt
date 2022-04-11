package com.example.storyapp.feature.story.data.repository

import com.example.storyapp.feature.story.data.source.api.StoryApiService
import com.example.storyapp.feature.story.domain.model.AddStoryResponse
import com.example.storyapp.feature.story.domain.model.StoryResponse
import com.example.storyapp.feature.story.domain.repository.StoryRepositoryService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyApiService: StoryApiService
) : StoryRepositoryService {
    override suspend fun getStory(token: String, page: Int?, size: Int?): StoryResponse {
        return storyApiService.getStory(token, page, size)
    }

    override suspend fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): AddStoryResponse {
        return storyApiService.addStory(token, file, description)
    }
}