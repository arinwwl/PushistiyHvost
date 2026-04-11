package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.data.model.AuthResult
import com.example.pushistiyhvost.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    companion object {
        private const val ADMIN_EMAIL = "admin@mail.ru"
        private const val ADMIN_PASSWORD = "123456"
    }

    override suspend fun register(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            AuthResult(isSuccess = true)
        } catch (e: Exception) {
            AuthResult(
                isSuccess = false,
                errorMessage = e.message
            )
        }
    }

    override suspend fun login(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()

            val isAdmin = email.trim() == ADMIN_EMAIL && password == ADMIN_PASSWORD

            AuthResult(
                isSuccess = true,
                isAdmin = isAdmin
            )
        } catch (e: Exception) {
            AuthResult(
                isSuccess = false,
                errorMessage = e.message
            )
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun resetPassword(email: String): AuthResult {
        return try {
            firebaseAuth.sendPasswordResetEmail(email.trim()).await()
            AuthResult(isSuccess = true)
        } catch (e: Exception) {
            AuthResult(
                isSuccess = false,
                errorMessage = e.message ?: "Ошибка восстановления"
            )
        }
    }
}