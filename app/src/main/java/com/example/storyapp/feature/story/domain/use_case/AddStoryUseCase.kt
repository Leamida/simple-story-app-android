package com.example.storyapp.feature.story.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.storyapp.core.util.Result
import com.example.storyapp.feature.story.data.repository.StoryRepository
import com.example.storyapp.feature.story.domain.model.AddStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AddStoryUseCase @Inject constructor(
    private val storyRepository: StoryRepository
) {
    private val _addStory = MutableLiveData<AddStoryResponse?>()
    operator fun invoke(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<AddStoryResponse?>?> = liveData {
        emit(Result.Loading)
        try {
            _addStory.value = storyRepository.addStory(token, file, description)
            val tempData: LiveData<Result<AddStoryResponse?>?> =
                _addStory.map { map -> Result.Success(map) }
            emitSource(tempData)

        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Result.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}