package com.example.pushistiyhvost.presentation.checkout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pushistiyhvost.data.repository.OrderRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.CreateOrderUseCase
import com.example.pushistiyhvost.presentation.cart.CartManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CheckoutScreen(
    onOrderConfirmed: () -> Unit
) {
    var currentStep by remember { mutableIntStateOf(1) }

    var deliveryType by remember { mutableStateOf("Доставка") }

    var street by remember { mutableStateOf("") }
    var house by remember { mutableStateOf("") }
    var apartment by remember { mutableStateOf("") }

    var pickupAddress by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf("Сегодня") }
    var selectedTime by remember { mutableStateOf("10:00 – 12:00") }
    var paymentType by remember { mutableStateOf("Карта") }

    val pickupAddresses = listOf(
        "г. Калининград, ул. Киевская, 55",
        "г. Калининград, ул. Театральная, 67"
    )

    val firestore = FirebaseFirestore.getInstance()
    val repository = OrderRepositoryImpl(firestore)
    val createOrderUseCase = CreateOrderUseCase(repository)

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
                text = "Оформление заказа",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Шаг $currentStep из 4",
                color = Color(0xFF8C889B)
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (currentStep) {
                1 -> {
                    Text(
                        text = "Способ получения",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SelectCard(
                        text = "Доставка",
                        selected = deliveryType == "Доставка",
                        onClick = {
                            deliveryType = "Доставка"
                            pickupAddress = ""
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SelectCard(
                        text = "Самовывоз",
                        selected = deliveryType == "Самовывоз",
                        onClick = {
                            deliveryType = "Самовывоз"
                            if (pickupAddress.isBlank()) {
                                pickupAddress = pickupAddresses.first()
                            }
                        }
                    )
                }

                2 -> {
                    Text(
                        text = if (deliveryType == "Самовывоз") "Адрес самовывоза" else "Адрес доставки",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (deliveryType == "Доставка") {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFE8E5F2)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Здесь будет карта",
                                    modifier = Modifier.padding(top = 56.dp),
                                    color = Color(0xFF6C6880)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = street,
                            onValueChange = { street = it },
                            label = { Text("Улица") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = house,
                            onValueChange = { house = it },
                            label = { Text("Дом") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = apartment,
                            onValueChange = { apartment = it },
                            label = { Text("Квартира") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                    } else {
                        Text(
                            text = "Выберите магазин для самовывоза",
                            color = Color(0xFF6C6880)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        pickupAddresses.forEach { address ->
                            SelectCard(
                                text = address,
                                selected = pickupAddress == address,
                                onClick = { pickupAddress = address }
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

                3 -> {
                    Text(
                        text = "Дата и время",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SelectCard(
                        text = "Сегодня",
                        selected = selectedDate == "Сегодня",
                        onClick = { selectedDate = "Сегодня" }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SelectCard(
                        text = "Завтра",
                        selected = selectedDate == "Завтра",
                        onClick = { selectedDate = "Завтра" }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Время",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SelectCard(
                        text = "10:00 – 12:00",
                        selected = selectedTime == "10:00 – 12:00",
                        onClick = { selectedTime = "10:00 – 12:00" }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SelectCard(
                        text = "12:00 – 14:00",
                        selected = selectedTime == "12:00 – 14:00",
                        onClick = { selectedTime = "12:00 – 14:00" }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SelectCard(
                        text = "14:00 – 18:00",
                        selected = selectedTime == "14:00 – 18:00",
                        onClick = { selectedTime = "14:00 – 18:00" }
                    )
                }

                4 -> {
                    Text(
                        text = "Оплата",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SelectCard(
                        text = "Карта",
                        selected = paymentType == "Карта",
                        onClick = { paymentType = "Карта" }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SelectCard(
                        text = "Apple Pay / Google Pay",
                        selected = paymentType == "Apple Pay / Google Pay",
                        onClick = { paymentType = "Apple Pay / Google Pay" }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SelectCard(
                        text = "При получении",
                        selected = paymentType == "При получении",
                        onClick = { paymentType = "При получении" }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (currentStep < 4) {
                        currentStep++
                    } else {
                        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button

                        val finalAddress = if (deliveryType == "Самовывоз") {
                            pickupAddress
                        } else {
                            buildString {
                                if (street.isNotBlank()) append("ул. ${street.trim()}")
                                if (house.isNotBlank()) {
                                    if (isNotBlank()) append(", ")
                                    append("д. ${house.trim()}")
                                }
                                if (apartment.isNotBlank()) {
                                    if (isNotBlank()) append(", ")
                                    append("кв. ${apartment.trim()}")
                                }
                            }
                        }

                        CoroutineScope(Dispatchers.IO).launch {
                            createOrderUseCase(
                                userId = userId,
                                items = CartManager.cartItems.toList(),
                                totalPrice = CartManager.getTotalPrice(),
                                deliveryType = deliveryType,
                                address = finalAddress,
                                date = selectedDate,
                                time = selectedTime,
                                paymentType = paymentType
                            )

                            CartManager.clearCart()

                            launch(Dispatchers.Main) {
                                onOrderConfirmed()
                            }
                        }
                    }
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
                    text = if (currentStep < 4) "Продолжить" else "Подтвердить заказ",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SelectCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = if (selected) Color(0xFFF6C542) else Color.White
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(18.dp),
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = Color(0xFF2B2740)
        )
    }
}