package com.example.storyapp.feature.auth.data.source.api

import com.example.storyapp.feature.auth.domain.model.LoginResponse
import com.example.storyapp.feature.auth.domain.model.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthApiService {

    @POST("login")
    suspend fun postLogin(
        @Field("email") email:String,
        @Field("password") password:String
    ):LoginResponse

    @POST("register")
    suspend fun postRegister(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password:String
    ):RegisterResponse

}