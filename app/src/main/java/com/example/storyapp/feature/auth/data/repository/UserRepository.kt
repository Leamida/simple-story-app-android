package com.example.storyapp.feature.auth.data.repository

import com.example.storyapp.feature.auth.data.source.local.preferences.UserPreferences
import com.example.storyapp.feature.auth.data.source.local.preferences.UserPreferencesService
import com.example.storyapp.feature.auth.domain.model.User
import com.example.storyapp.feature.auth.domain.repository.UserRepositoryService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userPreferences: UserPreferences
) :UserRepositoryService{
    override fun getUser(): Flow<User?> {
        return userPreferences.getUser()
    }

    override suspend fun setUser(user: User?) {
        return userPreferences.setUser(user)
    }
}