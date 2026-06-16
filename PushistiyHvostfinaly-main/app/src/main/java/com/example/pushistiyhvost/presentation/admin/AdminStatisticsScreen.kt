package com.example.pushistiyhvost.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.OrderRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.GetAdminStatisticsUseCase
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminStatisticsScreen() {
    val firestore = FirebaseFirestore.getInstance()
    val repository = OrderRepositoryImpl(firestore)

    val viewModel: AdminStatisticsViewModel = viewModel(
        factory = AdminStatisticsViewModelFactory(
            getAdminStatisticsUseCase = GetAdminStatisticsUseCase(repository)
        )
    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStatistics()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Статистика",
            style = MaterialTheme.typography.headlineMedium
        )

        when (val state = uiState) {
            AdminStatisticsUiState.Idle -> Unit

            AdminStatisticsUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminStatisticsUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is AdminStatisticsUiState.Success -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Всего заказов: ${state.statistics.totalOrders}")
                        Text("Общая выручка: ${state.statistics.totalRevenue} ₽")
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Популярные товары",
                            style = MaterialTheme.typography.titleMedium
                        )

                        if (state.statistics.popularProducts.isEmpty()) {
                            Text("Нет данных")
                        } else {
                            state.statistics.popularProducts.forEach { product ->
                                Text("${product.name} — ${product.count} шт.")
                            }
                        }
                    }
                }
            }
        }
    }
}