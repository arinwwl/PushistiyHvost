package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.domain.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl(
    private val firestore: FirebaseFirestore
) : ProfileRepository {

    override suspend fun getProfileImageBase64(userId: String): String? {
        return try {
            val document = firestore.collection("users")
                .document(userId)
                .get()
                .await()

            document.getString("profileImageBase64")
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun saveProfileImageBase64(
        userId: String,
        imageBase64: String
    ): Result<Unit> {
        return try {
            val data = mapOf("profileImageBase64" to imageBase64)

            firestore.collection("users")
                .document(userId)
                .set(data, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}