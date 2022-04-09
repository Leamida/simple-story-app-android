package com.example.storyapp.presentation.view_model

import androidx.lifecycle.ViewModel
import com.example.storyapp.feature.auth.domain.use_case.GetUserUseCase
import com.example.storyapp.feature.auth.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val loginUseCase: LoginUseCase
) :ViewModel(){
    fun postLogin(email:String,password:String) = loginUseCase(email, password)
    fun getUser()=getUserUseCase()
}