package com.example.storyapp.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.feature.auth.domain.model.User
import com.example.storyapp.feature.auth.domain.use_case.LoginUseCase
import com.example.storyapp.feature.auth.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {
    fun postLogin(email: String, password: String) = loginUseCase(email, password)
    fun postRegister(name: String, email: String, password: String) =
        registerUseCase(name, email, password)

}