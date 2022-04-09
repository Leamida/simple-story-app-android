package com.example.storyapp.feature.auth.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.storyapp.core.util.Result
import com.example.storyapp.feature.auth.data.repository.AuthRepository
import com.example.storyapp.feature.auth.data.repository.UserRepository
import com.example.storyapp.feature.auth.domain.model.User
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    private val _login = MutableLiveData<String>()
    operator fun invoke(email: String, password: String): LiveData<Result<String>> = liveData {
        emit(Result.Loading)
        try {
            val loginResponse = authRepository.postLogin(email, password)
            if (loginResponse.error) {
                emit(Result.Error(loginResponse.message))
            } else {
                _login.value = loginResponse.message
                loginResponse.loginResult?.let {
                    userRepository.setUser(it)
                }
                val tempData: LiveData<Result<String>> =
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