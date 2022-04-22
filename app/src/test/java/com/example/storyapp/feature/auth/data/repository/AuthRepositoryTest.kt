package com.example.storyapp.feature.auth.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp.feature.auth.data.source.api.AuthApiService
import com.example.storyapp.feature.auth.data.source.api.AuthDataDummy
import com.example.storyapp.feature.auth.data.source.api.FakeAuthApiService
import com.example.storyapp.feature.story.data.MainCoroutineRule
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var apiService: AuthApiService
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        apiService = FakeAuthApiService()
        authRepository = AuthRepository(apiService)
    }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `when postLogin with registered email and password, user Should Not Null`() =
        mainCoroutineRule.runBlockingTest {
            val loginResponse = apiService.postLogin("leo@gmail.com", "123456")
            val actualUser = loginResponse.loginResult
            val expectedUser =AuthDataDummy.generateDummyLoginResponse("leo@gmail.com", "123456").loginResult

            assertNotNull(actualUser)
            assertEquals(expectedUser,actualUser)
        }


    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `when postRegister should return Message`() =
        mainCoroutineRule.runBlockingTest {
            val expectedMessage = AuthDataDummy.generateDummyRegisterResponse("test", "registered@email.com", "123456").message
            val registerResponse = authRepository.postRegister("test", "registered@email.com", "123456")
            val actualMessage = registerResponse.message

            assertNotNull(actualMessage)
            assertEquals(expectedMessage,actualMessage)
        }
}