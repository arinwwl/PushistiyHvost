package com.example.pushistiyhvost.presentation.admin.model

data class AdminOrder(
    val id: String,
    val userId: String,
    val address: String,
    val deliveryType: String,
    val date: String,
    val time: String,
    val paymentType: String,
    val status: String,
    val totalPrice: Int,
    val items: List<AdminOrderItem>
)

data class AdminOrderItem(
    val name: String,
    val price: Int,
    val quantity: Int
)