package com.example.pushistiyhvost.presentation.orders.model

data class Order(
    val id: String,
    val totalPrice: Int,
    val status: String,
    val createdAt: Long,
    val deliveryType: String,
    val address: String,
    val date: String,
    val time: String,
    val paymentType: String
)