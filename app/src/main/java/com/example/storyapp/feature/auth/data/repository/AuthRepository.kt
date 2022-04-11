package com.example.storyapp.feature.auth.data.repository

import com.example.storyapp.feature.auth.data.source.api.AuthApiService
import com.example.storyapp.feature.auth.domain.model.LoginResponse
import com.example.storyapp.feature.auth.domain.model.RegisterResponse
import com.example.storyapp.feature.auth.domain.repository.AuthRepositoryService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRepositoryService {

    override suspend fun postLogin(email: String, password: String): LoginResponse {
        return authApiService.postLogin(email, password)
    }

    override suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {
        return authApiService.postRegister(name, email, password)
    }
}