package com.example.storyapp.feature.story.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.core.util.Result
import com.example.storyapp.feature.story.data.pager.StoryPagingSource
import com.example.storyapp.feature.story.data.repository.StoryRepository
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import retrofit2.HttpException
import java.io.IOException
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

    private val _stories = MutableLiveData<List<ListStoryItem?>?>()
    operator fun invoke(
        token: String,
        location :Int
    ): LiveData<Result<List<ListStoryItem?>?>> = liveData {
        emit(Result.Loading)
        try {
            val storyResponse = storyRepository.getStory(token, location)
            if (storyResponse.error) {
                emit(Result.Error(storyResponse.message))
            } else {
                _stories.value = storyResponse.listStory
                val tempData: LiveData<Result<List<ListStoryItem?>?>> =
                    _stories.map { map -> Result.Success(map) }
                emitSource(tempData)
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Result.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}