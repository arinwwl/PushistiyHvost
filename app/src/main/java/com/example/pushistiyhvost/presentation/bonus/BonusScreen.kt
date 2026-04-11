package com.example.pushistiyhvost.presentation.bonus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.pushistiyhvost.presentation.orders.OrdersViewModel
import com.example.pushistiyhvost.presentation.orders.OrdersViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun BonusScreen() {
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

    val totalSpent = orders.sumOf { it.totalPrice }
    val bonus = (totalSpent * 0.05).toInt()

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
                text = "Бонусы",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFEADFFF)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Ваш баланс", color = Color(0xFF6C6880))

                    Text(
                        text = "$bonus баллов",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Можно списать до $bonus ₽",
                        color = Color(0xFF6C6880)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "История начислений",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (orders.isEmpty()) {
                Text("Пока нет начислений")
            } else {
                orders.forEach { order ->
                    val orderBonus = (order.totalPrice * 0.05).toInt()

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Заказ ${order.id.takeLast(5)}")

                            Text(
                                text = "+$orderBonus баллов",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4A8F47)
                            )
                        }
                    }
                }
            }
        }
    }
}