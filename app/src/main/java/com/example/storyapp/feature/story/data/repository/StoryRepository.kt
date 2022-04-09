package com.example.storyapp.feature.story.data.repository

import com.example.storyapp.feature.story.data.source.api.StoryApiService
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.feature.story.domain.model.StoryResponse
import com.example.storyapp.feature.story.domain.repository.StoryRepositoryService
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyApiService: StoryApiService
) :StoryRepositoryService{
    override suspend fun getStory(token: String, page: Int?, size: Int?): StoryResponse {
        return storyApiService.getStory(token,page, size)
    }
}