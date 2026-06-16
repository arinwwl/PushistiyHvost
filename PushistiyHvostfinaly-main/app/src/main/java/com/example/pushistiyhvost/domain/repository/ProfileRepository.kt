package com.example.pushistiyhvost.domain.repository

interface ProfileRepository {
    suspend fun getProfileImageBase64(userId: String): String?
    suspend fun saveProfileImageBase64(userId: String, imageBase64: String): Result<Unit>
}