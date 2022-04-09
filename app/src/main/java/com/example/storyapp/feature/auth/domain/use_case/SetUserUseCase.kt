package com.example.storyapp.feature.auth.domain.use_case

import com.example.storyapp.feature.auth.data.repository.UserRepository
import com.example.storyapp.feature.auth.domain.model.User
import javax.inject.Inject

class SetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User){
        userRepository.setUser(user)
    }
}