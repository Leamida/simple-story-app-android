package com.example.storyapp.feature.story.domain.use_case

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.feature.story.data.pager.StoryPagingSource
import com.example.storyapp.feature.story.data.repository.StoryRepository
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import javax.inject.Inject

class GetStoriesUseCase @Inject constructor(
    private val storyRepository: StoryRepository
) {
    operator fun invoke(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                StoryPagingSource(storyRepository = storyRepository, token)
            }
        ).liveData
    }
}