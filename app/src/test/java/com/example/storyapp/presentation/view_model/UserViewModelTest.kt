package com.example.storyapp.presentation.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.core.util.Result
import com.example.storyapp.core.util.getOrAwaitValue
import com.example.storyapp.feature.auth.data.repository.AuthRepository
import com.example.storyapp.feature.auth.data.source.api.AuthDataDummy
import com.example.storyapp.feature.auth.data.source.api.FakeAuthApiService
import com.example.storyapp.feature.auth.domain.model.User
import com.example.storyapp.feature.auth.domain.use_case.LoginUseCase
import com.example.storyapp.feature.auth.domain.use_case.RegisterUseCase
import org.mockito.Mockito.`when`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var loginUseCase: LoginUseCase
    @Mock
    private lateinit var registerUseCase: RegisterUseCase

    private lateinit var authRepository : AuthRepository
    private lateinit var userViewModel: UserViewModel
    private val dummyAuth = AuthDataDummy

    @Before
    fun setUp() {
        authRepository = AuthRepository(FakeAuthApiService())
        userViewModel = UserViewModel(
            loginUseCase,
            registerUseCase
        )
    }

    @Test
    fun `when Login with registered email and password should Return Success`(){
        val expectedUser = MutableLiveData<Result<User?>>()
        expectedUser.value = Result.Success(dummyAuth.generateDummyLoginResponse("leo@gmail.com","123456").loginResult)

        `when`(userViewModel.postLogin("leo@gmail.com","123456")).thenReturn(expectedUser)

        val actualUser = userViewModel.postLogin("leo@gmail.com","123456").getOrAwaitValue()

        Mockito.verify(loginUseCase).invoke("leo@gmail.com","123456")

        assertNotNull(actualUser)
        assertTrue(actualUser is Result.Success)
        assertEquals(dummyAuth.generateDummyLoginResponse("leo@gmail.com","123456").loginResult,(actualUser as Result.Success).data)
    }

    @Test
    fun `when Register with unregister email and valid password should not Null and Return Success`(){
        val expectedResponse = MutableLiveData<Result<String>>()
        expectedResponse.value = Result.Success(dummyAuth.generateDummyRegisterResponse("leo","unregister@gmail.com","123456").message)

        `when`(userViewModel.postRegister("leo","unregister@gmail.com","123456")).thenReturn(expectedResponse)

        val actualResponse = userViewModel.postRegister("leo","unregister@gmail.com","123456").getOrAwaitValue()

        Mockito.verify(registerUseCase).invoke("leo","unregister@gmail.com","123456")

        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(dummyAuth.generateDummyRegisterResponse("leo","unregister@gmail.com","123456").message,(actualResponse as Result.Success).data)
    }

}