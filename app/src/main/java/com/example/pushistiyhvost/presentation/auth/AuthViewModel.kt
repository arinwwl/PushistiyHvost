package com.example.pushistiyhvost.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.LoginUseCase
import com.example.pushistiyhvost.domain.usecase.RegisterUseCase
import com.example.pushistiyhvost.domain.usecase.ResetPasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authState: StateFlow<AuthUiState> = _authState

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val result = registerUseCase(email, password)
            _authState.value = if (result.isSuccess) {
                AuthUiState.SuccessUser
            } else {
                AuthUiState.Error(result.errorMessage ?: "Ошибка регистрации")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val result = loginUseCase(email, password)

            _authState.value = when {
                !result.isSuccess -> AuthUiState.Error(result.errorMessage ?: "Ошибка входа")
                result.isAdmin -> AuthUiState.SuccessAdmin
                else -> AuthUiState.SuccessUser
            }
        }
    }

    fun resetState() {
        _authState.value = AuthUiState.Idle
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val result = resetPasswordUseCase(email)

            _authState.value = if (result.isSuccess) {
                AuthUiState.SuccessUser
            } else {
                AuthUiState.Error(result.errorMessage ?: "Ошибка восстановления")
            }
        }
    }
}