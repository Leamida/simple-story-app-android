package com.example.storyapp.feature.auth.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.storyapp.core.util.Result
import com.example.storyapp.feature.auth.data.repository.AuthRepository
import com.example.storyapp.feature.auth.data.source.local.preferences.UserPreferences
import com.example.storyapp.feature.auth.domain.model.LoginResponse
import com.example.storyapp.feature.auth.domain.model.User
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    private val _login = MutableLiveData<User?>()
    operator fun invoke(email: String, password: String): LiveData<Result<User?>> = liveData {
        emit(Result.Loading)
        try {
            val loginResponse = authRepository.postLogin(email, password)
            if (loginResponse.error) {
                emit(Result.Error(loginResponse.message))
            } else {
                _login.value = loginResponse.loginResult
                val tempData: LiveData<Result<User?>> =
                    _login.map { map -> Result.Success(map) }
                emitSource(tempData)
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Result.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}