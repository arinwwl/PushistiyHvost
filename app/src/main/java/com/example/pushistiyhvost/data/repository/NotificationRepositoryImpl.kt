package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.domain.repository.NotificationRepository
import com.example.pushistiyhvost.presentation.admin.model.AdminNotification
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotificationRepositoryImpl(
    private val firestore: FirebaseFirestore
) : NotificationRepository {

    override suspend fun sendNotification(notification: AdminNotification): Result<Unit> {
        return try {
            val documentRef = firestore.collection("notifications").document()

            val data = hashMapOf(
                "text" to notification.text,
                "timestamp" to notification.timestamp
            )

            documentRef.set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}