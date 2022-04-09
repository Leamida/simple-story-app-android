package com.example.storyapp.feature.auth.domain.model

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)