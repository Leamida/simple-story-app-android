package com.example.storyapp.feature.auth.domain.repository

import com.example.storyapp.feature.auth.domain.model.LoginResponse
import com.example.storyapp.feature.auth.domain.model.RegisterResponse

interface AuthRepositoryService {
    suspend fun postLogin(email:String,password:String):LoginResponse
    suspend fun postRegister(name:String,email:String,password:String):RegisterResponse
}