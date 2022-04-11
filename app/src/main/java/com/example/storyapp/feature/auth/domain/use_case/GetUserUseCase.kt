package com.example.storyapp.feature.auth.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.storyapp.feature.auth.data.repository.UserRepository
import com.example.storyapp.feature.auth.domain.model.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): LiveData<User?> = liveData {
        emitSource(userRepository.getUser().asLiveData())
    }
}