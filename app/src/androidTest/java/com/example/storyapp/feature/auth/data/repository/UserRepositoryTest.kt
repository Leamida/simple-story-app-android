package com.example.storyapp.feature.auth.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.example.storyapp.feature.auth.data.source.local.preferences.UserPreferences
import com.example.storyapp.feature.auth.domain.model.User
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class UserRepositoryTest {


    private lateinit var userRepository: UserRepository
    private lateinit var instrumentedContext: Context

    @Before
    fun setUp() {
        instrumentedContext =
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        userRepository = UserRepository(UserPreferences(instrumentedContext))
    }

    @Test
    fun getUser() {
        val userPreferences = userRepository.getUser()
        var user: User? = null
        userPreferences.map {
            user = it
        }
        if (user == null){
            println("user null")
        }
    }

//    @Test
//    fun setUser() = runBlocking{
//        userRepository.setUser(User("test","testId","token"))
//    }
}