package com.example.pushistiyhvost.presentation.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun AdminPromotionsScreen(
    onCreatePromotionClick: () -> Unit,
    onEditPromotionClick: (String) -> Unit
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

    val promotions by viewModel.promotions.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val search = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadPromotions()
    }

    val filteredPromotions = promotions.filter { promotion ->
        promotion.title.contains(search.value, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Акции",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = search.value,
            onValueChange = { search.value = it },
            label = { Text("Поиск") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onCreatePromotionClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Создать акцию")
        }

        when (val currentState = uiState) {
            AdminPromotionsUiState.Idle -> Unit

            AdminPromotionsUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminPromotionsUiState.Success -> {
                Text(currentState.message)
            }

            is AdminPromotionsUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredPromotions) { promotion ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEditPromotionClick(promotion.id) },
                    colors = CardDefaults.cardColors()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = promotion.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Скидка: ${promotion.discount}")
                        Text("Дата: ${promotion.date}")
                    }
                }
            }
        }
    }
}