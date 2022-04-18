package com.example.storyapp.feature.story.data.source.api

import com.example.storyapp.feature.story.domain.model.AddStoryResponse
import com.example.storyapp.feature.story.domain.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeStoryApiService : StoryApiService {

    private val dummyResponse = StoryDataDummy.generateDummyStoriesResponse()
    override suspend fun getStory(token: String, page: Int?, size: Int?): StoryResponse {
        return dummyResponse
    }

    override suspend fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddStoryResponse {
        TODO("Not yet implemented")
    }
}