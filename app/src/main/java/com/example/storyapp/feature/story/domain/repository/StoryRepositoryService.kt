package com.example.storyapp.feature.story.domain.repository

import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.feature.story.domain.model.StoryResponse

interface StoryRepositoryService {

    suspend fun getStory(token:String,page:Int?,size:Int?):StoryResponse
}