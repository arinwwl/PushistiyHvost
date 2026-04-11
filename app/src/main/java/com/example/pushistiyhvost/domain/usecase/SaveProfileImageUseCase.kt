package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.ProfileRepository

class SaveProfileImageUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String, imageBase64: String): Result<Unit> {
        return repository.saveProfileImageBase64(userId, imageBase64)
    }
}