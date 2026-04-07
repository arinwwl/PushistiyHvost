package com.example.pushistiyhvost.data.model

data class OrderDto(
    val id: String = "",
    val userId: String = "",
    val items: List<OrderItemDto> = emptyList(),
    val totalPrice: Int = 0,
    val status: String = "",
    val createdAt: Long = 0L,
    val deliveryType: String = "",
    val address: String = "",
    val date: String = "",
    val time: String = "",
    val paymentType: String = ""
)