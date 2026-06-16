package com.example.pushistiyhvost.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.GetProfileImageUseCase
import com.example.pushistiyhvost.domain.usecase.SaveProfileImageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    data object Idle : ProfileUiState()
    data object Loading : ProfileUiState()
    data class Success(val message: String) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(
    private val getProfileImageUseCase: GetProfileImageUseCase,
    private val saveProfileImageUseCase: SaveProfileImageUseCase
) : ViewModel() {

    private val _profileImageBase64 = MutableStateFlow<String?>(null)
    val profileImageBase64: StateFlow<String?> = _profileImageBase64

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun loadProfileImage(userId: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                _profileImageBase64.value = getProfileImageUseCase(userId)
                _uiState.value = ProfileUiState.Idle
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(
                    e.message ?: "Ошибка загрузки фото профиля"
                )
            }
        }
    }

    fun saveProfileImage(userId: String, imageBase64: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            val result = saveProfileImageUseCase(userId, imageBase64)

            result.fold(
                onSuccess = {
                    _profileImageBase64.value = imageBase64
                    _uiState.value = ProfileUiState.Success("Фото профиля обновлено")
                },
                onFailure = { error ->
                    _uiState.value = ProfileUiState.Error(
                        error.message ?: "Ошибка сохранения фото"
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = ProfileUiState.Idle
    }
}