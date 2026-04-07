package com.example.pushistiyhvost.presentation.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.OrderRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.GetOrdersUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen() {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "guest"

    val firestore = FirebaseFirestore.getInstance()
    val repository = OrderRepositoryImpl(firestore)
    val getOrdersUseCase = GetOrdersUseCase(repository)

    val viewModel: OrdersViewModel = viewModel(
        factory = OrdersViewModelFactory(getOrdersUseCase)
    )

    val orders by viewModel.orders.collectAsState()

    LaunchedEffect(userId) {
        if (userId != "guest") {
            viewModel.loadOrders(userId)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Заказы",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (userId == "guest") {
                Text("В гостевом режиме история заказов недоступна")
                return@Column
            }

            if (orders.isEmpty()) {
                Text("У вас пока нет заказов")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders) { order ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            tonalElevation = 1.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Заказ №${order.id.takeLast(6)}",
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Дата: ${formatDate(order.createdAt)}",
                                    color = Color(0xFF6C6880)
                                )

                                Text(
                                    text = "Сумма: ${order.totalPrice} ₽",
                                    fontWeight = FontWeight.Medium
                                )

                                Text(
                                    text = "Статус: ${order.status}",
                                    color = Color(0xFF4A8F47)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    return try {
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        formatter.format(Date(timestamp))
    } catch (e: Exception) {
        "-"
    }
}