package com.example.storyapp.presentation.view_model

import androidx.lifecycle.ViewModel
import com.example.storyapp.feature.story.domain.use_case.GetStoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val getStoriesUseCase: GetStoriesUseCase
):ViewModel(){

    fun getStories(token:String,page:Int?=null,size:Int?=null) = getStoriesUseCase(token,page, size)

}