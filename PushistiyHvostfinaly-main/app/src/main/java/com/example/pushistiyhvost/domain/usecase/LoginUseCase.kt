package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.login(email, password)
}