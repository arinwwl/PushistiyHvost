package com.example.pushistiyhvost.domain.repository

import com.example.pushistiyhvost.data.model.AuthResult

interface AuthRepository {
    suspend fun register(email: String, password: String): AuthResult
    suspend fun login(email: String, password: String): AuthResult
    fun logout()
    fun isUserLoggedIn(): Boolean

    suspend fun resetPassword(email: String): AuthResult
}