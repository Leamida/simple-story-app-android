package com.example.storyapp.feature.story.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.storyapp.feature.story.data.pager.StoryPagingSource
import com.example.storyapp.feature.story.data.source.api.StoryApiService
import com.example.storyapp.feature.story.domain.model.AddStoryResponse
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import com.example.storyapp.feature.story.domain.model.StoryResponse
import com.example.storyapp.feature.story.domain.repository.StoryRepositoryService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val storyApiService: StoryApiService
) : StoryRepositoryService {
    override fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingSource(apiService = storyApiService,token)
            }
        ).liveData
    }

    override suspend fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddStoryResponse {
        return storyApiService.addStory(token, file, description,lat, lon)
    }


}