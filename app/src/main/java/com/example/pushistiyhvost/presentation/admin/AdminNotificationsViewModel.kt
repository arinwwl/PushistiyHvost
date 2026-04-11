package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.SendNotificationUseCase
import com.example.pushistiyhvost.presentation.admin.model.AdminNotification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AdminNotificationsUiState {
    data object Idle : AdminNotificationsUiState()
    data object Loading : AdminNotificationsUiState()
    data class Success(val message: String) : AdminNotificationsUiState()
    data class Error(val message: String) : AdminNotificationsUiState()
}

class AdminNotificationsViewModel(
    private val sendNotificationUseCase: SendNotificationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminNotificationsUiState>(AdminNotificationsUiState.Idle)
    val uiState: StateFlow<AdminNotificationsUiState> = _uiState

    fun sendNotification(
        text: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = AdminNotificationsUiState.Loading

            if (text.isBlank()) {
                _uiState.value = AdminNotificationsUiState.Error("Введите текст уведомления")
                return@launch
            }

            val notification = AdminNotification(
                text = text.trim(),
                timestamp = System.currentTimeMillis()
            )

            val result = sendNotificationUseCase(notification)

            result.fold(
                onSuccess = {
                    _uiState.value = AdminNotificationsUiState.Success("Уведомление отправлено")
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.value = AdminNotificationsUiState.Error(
                        error.message ?: "Ошибка отправки уведомления"
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = AdminNotificationsUiState.Idle
    }
}