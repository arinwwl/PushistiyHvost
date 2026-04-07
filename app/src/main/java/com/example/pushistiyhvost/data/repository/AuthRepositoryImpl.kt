package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.data.model.AuthResult
import com.example.pushistiyhvost.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

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
            AuthResult(isSuccess = true)
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
}