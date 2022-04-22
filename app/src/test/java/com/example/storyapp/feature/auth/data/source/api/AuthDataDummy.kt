package com.example.storyapp.feature.auth.data.source.api

import com.example.storyapp.feature.auth.domain.model.LoginResponse
import com.example.storyapp.feature.auth.domain.model.RegisterResponse
import com.example.storyapp.feature.auth.domain.model.User
import java.util.regex.Pattern

object AuthDataDummy {
    fun generateDummyLoginResponse(email: String, password: String): LoginResponse {
        val registeredEmail = "leo@gmail.com"
        val registeredPassword = "123456"
        val user = User(
            "leo",
            "user-NEY5gEghBcNXOOny",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLU5FWTVnRWdoQmNOWE9PbnkiLCJpYXQiOjE2NTAyNTg3NDh9.tZ1N-A_FVk5NAt83bqtj3a_jSXIoKWmJTQRPvoMMs_k"
        )
        when {
            email != registeredEmail -> {
                return LoginResponse(null,true,"User not found")
            }
            email != registeredEmail && password != registeredPassword -> {
                return LoginResponse(null,true,"Invalid password")
            }
            !Pattern.matches(
                "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
                email
            ) ->{
                return LoginResponse(null,true,"\"email\" must be a valid email")
            }
            else ->{
                return LoginResponse(user, false, "success")
            }
        }
    }

    fun generateDummyRegisterResponse(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {
        val registeredEmail = "registered@email.com"
        when {
            email == registeredEmail -> {
                return RegisterResponse(true, "Email is already taken")
            }
            password.length < 6 -> {
                return RegisterResponse(true, "Password must be at least 6 characters long")
            }
            !Pattern.matches(
                "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
                email
            ) -> {
                return RegisterResponse(true, "\"email\" must be a valid email")
            }
            else -> {
                return RegisterResponse(false, "User created")
            }
        }
    }
}