package com.example.pushistiyhvost.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.LoginUseCase
import com.example.pushistiyhvost.domain.usecase.RegisterUseCase

class AuthViewModelFactory(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(registerUseCase, loginUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}