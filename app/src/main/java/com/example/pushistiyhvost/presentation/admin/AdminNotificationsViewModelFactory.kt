package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.SendNotificationUseCase

class AdminNotificationsViewModelFactory(
    private val sendNotificationUseCase: SendNotificationUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminNotificationsViewModel::class.java)) {
            return AdminNotificationsViewModel(
                sendNotificationUseCase = sendNotificationUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}