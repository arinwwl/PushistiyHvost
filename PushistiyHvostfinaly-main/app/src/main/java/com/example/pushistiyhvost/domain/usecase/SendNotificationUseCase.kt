package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.NotificationRepository
import com.example.pushistiyhvost.presentation.admin.model.AdminNotification

class SendNotificationUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notification: AdminNotification): Result<Unit> {
        return repository.sendNotification(notification)
    }
}