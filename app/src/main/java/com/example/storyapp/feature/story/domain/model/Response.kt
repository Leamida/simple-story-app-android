package com.example.storyapp.feature.story.domain.model

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)