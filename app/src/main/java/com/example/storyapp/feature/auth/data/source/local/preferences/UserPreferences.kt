package com.example.storyapp.feature.auth.data.source.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.feature.auth.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("user")


class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) : UserPreferencesService {

    private val userDataStore = context.dataStore

    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_TOKEN = stringPreferencesKey("user_token")
    private val USER_ID = stringPreferencesKey("user_id")

    override fun getUser(): Flow<User?> = userDataStore.data.map {
        User(
            name = it[USER_NAME] ?: "",
            token = it[USER_TOKEN] ?: "",
            userId = it[USER_ID] ?: ""
        )
    }

    override suspend fun setUser(user: User?) {
        if (user != null) {
            userDataStore.edit {
                it[USER_NAME] = user.name
                it[USER_TOKEN] = user.token
                it[USER_ID] = user.userId
            }
        } else {
            userDataStore.edit {
                it.clear()
            }
        }
    }
}