package com.example.pushistiyhvost.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import androidx.compose.foundation.clickable

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onLoginRequiredClick: () -> Unit,
    onPetsClick: () -> Unit,
    onBonusClick: () -> Unit,
    onPromotionsClick: () -> Unit,
    onArticlesClick: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val isGuest = currentUser == null

    val firestore = FirebaseFirestore.getInstance()
    val repository = OrderRepositoryImpl(firestore)
    val getOrdersUseCase = GetOrdersUseCase(repository)

    val ordersViewModel: OrdersViewModel = viewModel(
        factory = OrdersViewModelFactory(getOrdersUseCase)
    )

    val orders by ordersViewModel.orders.collectAsState()

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { userId ->
            ordersViewModel.loadOrders(userId)
        }
    }

    val displayName = currentUser?.email
        ?.substringBefore("@")
        ?.replaceFirstChar { it.uppercase() }
        ?: "Гость"

    val displayEmail = currentUser?.email ?: "Вход не выполнен"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.size(90.dp),
                shape = CircleShape,
                color = Color(0xFFD9D9D9)
            ) {}

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = displayName,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = displayEmail,
                color = Color(0xFF7A768C),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Профиль",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Text("Статус: ${if (isGuest) "Гостевой режим" else "Авторизован"}")

                    if (!isGuest) {
                        Text("Количество заказов: ${orders.size}")
                        Text(
                            text = "Бонусы",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onBonusClick() }
                        )
                        Text(
                            text = "Акции",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onPromotionsClick() }
                        )
                        Text(
                            text = "Статьи",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onArticlesClick() }
                        )
                    }

                    if (!isGuest) {
                        Text(
                            text = "Мои питомцы",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onPetsClick() }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isGuest) {
                Button(
                    onClick = onLoginRequiredClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF6C542),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Войти или зарегистрироваться",
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogoutClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF6C542),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Выйти",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}