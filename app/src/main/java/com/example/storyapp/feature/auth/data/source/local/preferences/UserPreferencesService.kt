package com.example.storyapp.feature.auth.data.source.local.preferences

import com.example.storyapp.feature.auth.domain.model.User
import dagger.Provides
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface UserPreferencesService {

    fun getUser(): Flow<User?>
    suspend fun setUser(user: User?)
}