package com.example.storyapp.feature.story.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.storyapp.R
import com.example.storyapp.feature.story.data.repository.StoryRepository
import com.example.storyapp.feature.story.domain.model.ListStoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.storyapp.core.util.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class GetStoriesUseCase @Inject constructor(
    private val storyRepository: StoryRepository
) {
    private val _stories = MutableLiveData<List<ListStoryItem?>?>()
    private val stories: LiveData<List<ListStoryItem?>?> = _stories
    operator fun invoke(token:String):LiveData<Result<List<ListStoryItem?>?>> = liveData {
        emit(Result.Loading)
        try {
            _stories.value=storyRepository.getStory(token)
            val tempData: LiveData<Result<List<ListStoryItem?>?>> =
                stories.map { map -> Result.Success(map) }
            emitSource(tempData)
        }catch (e:HttpException){
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }catch (e:IOException){
            emit(Result.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}