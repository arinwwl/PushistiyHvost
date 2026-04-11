package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.ProfileRepository

class GetProfileImageUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(userId: String): String? {
        return repository.getProfileImageBase64(userId)
    }
}