package com.example.pushistiyhvost.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.AuthRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.LoginUseCase
import com.example.pushistiyhvost.domain.usecase.RegisterUseCase
import com.google.firebase.auth.FirebaseAuth
import com.example.pushistiyhvost.domain.usecase.ResetPasswordUseCase

@Composable
fun AuthScreen() {
    val firebaseAuth = FirebaseAuth.getInstance()
    val repository = AuthRepositoryImpl(firebaseAuth)
    val registerUseCase = RegisterUseCase(repository)
    val loginUseCase = LoginUseCase(repository)
    val resetPasswordUseCase = ResetPasswordUseCase(repository)
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            registerUseCase,
            loginUseCase,
            resetPasswordUseCase
        )
    )

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.register(email, password) }) {
            Text("Регистрация")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.login(email, password) }) {
            Text("Вход")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val currentState = state) {
            AuthUiState.Idle -> Unit
            AuthUiState.Loading -> CircularProgressIndicator()
            AuthUiState.Success -> Text("Успех")
            is AuthUiState.Error -> Text(currentState.message)
        }
    }
}