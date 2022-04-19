package com.example.storyapp.feature.story.data.pager

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.feature.story.data.repository.StoryRepository
import com.example.storyapp.feature.story.data.source.api.StoryApiService
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import javax.inject.Inject

class StoryPagingSource @Inject constructor(
    private val storyRepository: StoryRepository,
    private val token :String
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            Log.d("pagers","call pager")
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = storyRepository.getStory(token,page,params.loadSize).listStory
            LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}