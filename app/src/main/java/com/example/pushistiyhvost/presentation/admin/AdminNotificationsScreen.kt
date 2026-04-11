package com.example.pushistiyhvost.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.NotificationRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.SendNotificationUseCase
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminNotificationsScreen() {
    val firestore = FirebaseFirestore.getInstance()
    val repository = NotificationRepositoryImpl(firestore)

    val viewModel: AdminNotificationsViewModel = viewModel(
        factory = AdminNotificationsViewModelFactory(
            sendNotificationUseCase = SendNotificationUseCase(repository)
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    var text by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is AdminNotificationsUiState.Success) {
            text = ""
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Уведомления",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Текст уведомления") },
            modifier = Modifier.fillMaxWidth()
        )

        when (val currentState = uiState) {
            AdminNotificationsUiState.Idle -> Unit

            AdminNotificationsUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminNotificationsUiState.Success -> {
                Text(currentState.message)
            }

            is AdminNotificationsUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Button(
            onClick = {
                viewModel.sendNotification(text) {}
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Отправить")
        }
    }
}