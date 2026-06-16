package com.example.pushistiyhvost.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.OrderRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.GetAllOrdersUseCase
import com.example.pushistiyhvost.domain.usecase.GetOrderByIdAdminUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateOrderStatusUseCase
import com.example.pushistiyhvost.presentation.orders.model.Order
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminOrdersScreen(
    onOrderClick: (String) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    val repository = OrderRepositoryImpl(firestore)

    val viewModel: AdminOrdersViewModel = viewModel(
        factory = AdminOrdersViewModelFactory(
            getAllOrdersUseCase = GetAllOrdersUseCase(repository),
            getOrderByIdAdminUseCase = GetOrderByIdAdminUseCase(repository),
            updateOrderStatusUseCase = UpdateOrderStatusUseCase(repository)
        )
    )

    val orders by viewModel.orders.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val selectedFilter = remember { mutableStateOf("Все") }

    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }

    val filteredOrders = orders.filter { order ->
        selectedFilter.value == "Все" || order.status == selectedFilter.value
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Заказы",
            style = MaterialTheme.typography.headlineMedium
        )

        StatusFilterRow(
            selectedStatus = selectedFilter.value,
            onStatusSelected = { selectedFilter.value = it }
        )

        when (val currentState = uiState) {
            AdminOrdersUiState.Idle -> Unit

            AdminOrdersUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminOrdersUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is AdminOrdersUiState.Success -> {
                Text(currentState.message)
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredOrders) { order ->
                OrderCard(
                    order = order,
                    onClick = { onOrderClick(order.id) }
                )
            }
        }
    }
}

@Composable
private fun StatusFilterRow(
    selectedStatus: String,
    onStatusSelected: (String) -> Unit
) {
    val statuses = listOf("Все", "Создан", "Собирается", "В доставке", "Завершён")

    LazyColumn {
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                statuses.take(3).forEach { status ->
                    FilterChip(
                        text = status,
                        selected = selectedStatus == status,
                        onClick = { onStatusSelected(status) }
                    )
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                statuses.drop(3).forEach { status ->
                    FilterChip(
                        text = status,
                        selected = selectedStatus == status,
                        onClick = { onStatusSelected(status) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) Color(0xFFDCCEFF) else Color(0xFFF1ECF8)
    val textColor = if (selected) Color(0xFF5E35B1) else Color(0xFF4A4458)

    Box(
        modifier = Modifier
            .background(background, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor
        )
    }
}

@Composable
private fun OrderCard(
    order: Order,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Открыть заказ",
                color = Color.Red,
                modifier = Modifier.clickable {
                    onClick()
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Заказ: ${order.id.take(8)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text("Сумма: ${order.totalPrice} ₽")
                    Text("Получение: ${order.deliveryType}")
                }

                StatusBadge(status = order.status)
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val background = when (status) {
        "Создан" -> Color(0xFFFFF3CD)
        "Собирается" -> Color(0xFFFFE0B2)
        "В доставке" -> Color(0xFFD1C4E9)
        "Завершён" -> Color(0xFFC8E6C9)
        else -> Color(0xFFEDE7F6)
    }

    val textColor = when (status) {
        "Создан" -> Color(0xFF8A6D3B)
        "Собирается" -> Color(0xFFE65100)
        "В доставке" -> Color(0xFF5E35B1)
        "Завершён" -> Color(0xFF2E7D32)
        else -> Color(0xFF4A4458)
    }

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .background(background, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = status,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}