package com.example.storyapp.feature.auth.data.source.api

import com.example.storyapp.feature.auth.domain.model.LoginResponse
import com.example.storyapp.feature.auth.domain.model.RegisterResponse

class FakeAuthApiService : AuthApiService {


    override suspend fun postLogin(email: String, password: String): LoginResponse {
        return AuthDataDummy.generateDummyLoginResponse(email, password)
    }

    override suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {
        return AuthDataDummy.generateDummyRegisterResponse(name, email, password)
    }
}