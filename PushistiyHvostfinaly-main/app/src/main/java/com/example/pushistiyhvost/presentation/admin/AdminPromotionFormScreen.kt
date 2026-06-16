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
import com.example.pushistiyhvost.data.repository.PromotionRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.AddPromotionUseCase
import com.example.pushistiyhvost.domain.usecase.GetPromotionByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetPromotionsUseCase
import com.example.pushistiyhvost.domain.usecase.UpdatePromotionUseCase
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminPromotionFormScreen(
    promotionId: String?,
    onSaved: () -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    val repository = PromotionRepositoryImpl(firestore)

    val viewModel: AdminPromotionsViewModel = viewModel(
        factory = AdminPromotionsViewModelFactory(
            getPromotionsUseCase = GetPromotionsUseCase(repository),
            getPromotionByIdUseCase = GetPromotionByIdUseCase(repository),
            addPromotionUseCase = AddPromotionUseCase(repository),
            updatePromotionUseCase = UpdatePromotionUseCase(repository)
        )
    )

    val selectedPromotion by viewModel.selectedPromotion.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var banner by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    LaunchedEffect(promotionId) {
        if (!promotionId.isNullOrBlank()) {
            viewModel.loadPromotionById(promotionId)
        } else {
            viewModel.clearSelectedPromotion()
        }
    }

    LaunchedEffect(selectedPromotion) {
        selectedPromotion?.let { promotion ->
            title = promotion.title
            discount = promotion.discount
            banner = promotion.banner
            date = promotion.date
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (promotionId == null) "Создать акцию" else "Редактировать акцию",
            style = MaterialTheme.typography.headlineMedium
        )

        when (val currentState = uiState) {
            AdminPromotionsUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminPromotionsUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is AdminPromotionsUiState.Success -> {
                Text(currentState.message)
            }

            AdminPromotionsUiState.Idle -> Unit
        }

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = discount,
            onValueChange = { discount = it },
            label = { Text("Скидка") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = banner,
            onValueChange = { banner = it },
            label = { Text("Баннер") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Дата") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.savePromotion(
                    promotionId = promotionId,
                    title = title,
                    discount = discount,
                    banner = banner,
                    date = date,
                    onSuccess = onSaved
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
}