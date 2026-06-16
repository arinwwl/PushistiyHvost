package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.register(email, password)
}