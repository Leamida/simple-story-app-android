package com.example.storyapp.feature.story.domain.repository

import com.example.storyapp.feature.story.domain.model.ListStoryItem

interface StoryRepositoryService {

    suspend fun getStory(token:String):List<ListStoryItem?>?
}