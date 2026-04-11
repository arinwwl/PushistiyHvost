package com.example.pushistiyhvost.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.OrderRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.GetAllOrdersUseCase
import com.example.pushistiyhvost.domain.usecase.GetOrderByIdAdminUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateOrderStatusUseCase
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminOrderDetailsScreen(
    orderId: String,
    onBack: () -> Unit
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

    val order by viewModel.selectedOrder.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(orderId) {
        viewModel.loadOrderById(orderId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Детали заказа",
            style = MaterialTheme.typography.headlineMedium
        )

        when (val state = uiState) {
            AdminOrdersUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminOrdersUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is AdminOrdersUiState.Success -> {
                Text(state.message)
            }

            AdminOrdersUiState.Idle -> Unit
        }

        order?.let { currentOrder ->
            Text("ID: ${currentOrder.id}")
            Text("Пользователь: ${currentOrder.userId}")
            Text("Получение: ${currentOrder.deliveryType}")

            if (currentOrder.deliveryType == "Доставка") {
                Text("Адрес: ${currentOrder.address}")
            }

            Text("Дата: ${currentOrder.date}")
            Text("Время: ${currentOrder.time}")
            Text("Оплата: ${currentOrder.paymentType}")
            Text("Сумма: ${currentOrder.totalPrice} ₽")

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Текущий статус",
                    style = MaterialTheme.typography.titleMedium
                )
                DetailStatusBadge(status = currentOrder.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Состав заказа",
                style = MaterialTheme.typography.titleMedium
            )

            currentOrder.items.forEach { orderItem ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(orderItem.name)
                        Text("Цена: ${orderItem.price} ₽")
                        Text("Количество: ${orderItem.quantity}")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Изменение статуса",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = {
                    viewModel.updateStatus(orderId, "Собирается")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Собирается")
            }

            Button(
                onClick = {
                    viewModel.updateStatus(orderId, "В доставке")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("В доставке")
            }

            Button(
                onClick = {
                    viewModel.updateStatus(orderId, "Завершён")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Завершён")
            }

            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад")
            }
        }
    }
}

@Composable
private fun DetailStatusBadge(status: String) {
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
            .background(background, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = status,
            color = textColor
        )
    }
}