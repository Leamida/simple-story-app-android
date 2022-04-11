package com.example.storyapp.feature.auth.domain.repository

import com.example.storyapp.feature.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepositoryService {
    fun getUser(): Flow<User?>
    suspend fun setUser(user: User?)
}