package com.example.pushistiyhvost.presentation.orders.model

data class Order(
    val id: String,
    val userId: String,
    val items: List<OrderItem>,
    val totalPrice: Int,
    val status: String,
    val createdAt: Long,
    val deliveryType: String,
    val address: String,
    val date: String,
    val time: String,
    val paymentType: String
)

data class OrderItem(
    val productId: String,
    val name: String,
    val price: Int,
    val quantity: Int
)