package com.example.pushistiyhvost.domain.repository

import com.example.pushistiyhvost.presentation.admin.model.AdminNotification

interface NotificationRepository {
    suspend fun sendNotification(notification: AdminNotification): Result<Unit>
}