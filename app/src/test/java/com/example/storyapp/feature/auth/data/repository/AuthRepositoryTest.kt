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
            val user = loginResponse.loginResult

            assertNotNull(user)

        }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `when postLogin with unregister email and password, error should be true`() =
        mainCoroutineRule.runBlockingTest {
            val loginResponse = apiService.postLogin("unregister@gmail.com", "123456")
            val expectedError = true
            val actualError = loginResponse.error

            assertEquals(expectedError, actualError)

        }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `when postLogin with invalid email and password, error should be true`() =
        mainCoroutineRule.runBlockingTest {
            val expectedError = true
            val loginResponse = apiService.postLogin("invalid@email", "12345")
            val actualError = loginResponse.error

            assertEquals(expectedError, actualError)

        }


    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `when postRegister With Unregister email and 6 char password, error should be false`() =
        mainCoroutineRule.runBlockingTest {
            val expectedError = false
            val registerResponse =
                apiService.postRegister("test", "unregister@email.com", "123456")
            val actualError = registerResponse.error

            assertEquals(expectedError, actualError)
        }

    @Test
    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun `when postRegister With registered email and 6 char password, error should be true`() =
        mainCoroutineRule.runBlockingTest {
            val expectedError = true
            val registerResponse = apiService.postRegister("test", "registered@email.com", "123456")
            val actualError = registerResponse.error

            assertEquals(expectedError, actualError)
        }
}