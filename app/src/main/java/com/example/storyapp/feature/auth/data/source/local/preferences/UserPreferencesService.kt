package com.example.storyapp.feature.auth.data.source.local.preferences

import com.example.storyapp.feature.auth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserPreferencesService {

    fun getUser(): Flow<User?>
    suspend fun setUser(user: User?)
}