package com.example.storyapp.presentation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.feature.auth.domain.model.User
import com.example.storyapp.feature.auth.domain.use_case.GetUserUseCase
import com.example.storyapp.feature.auth.domain.use_case.LoginUseCase
import com.example.storyapp.feature.auth.domain.use_case.RegisterUseCase
import com.example.storyapp.feature.auth.domain.use_case.RemoveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val removeUserUseCase: RemoveUserUseCase
) :ViewModel(){
    fun postLogin(email:String,password:String) = loginUseCase(email, password)
    fun postRegister(name:String,email:String,password:String) = registerUseCase(name,email, password)
    fun getUser()=getUserUseCase()
    fun setUser(user: User?){
        viewModelScope.launch {
            removeUserUseCase(user)
        }
    }
}