package com.example.pushistiyhvost.presentation.auth

sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data object SuccessUser : AuthUiState()
    data object SuccessAdmin : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}