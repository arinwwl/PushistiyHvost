package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.AuthRepository

class ResetPasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String) =
        repository.resetPassword(email)
}