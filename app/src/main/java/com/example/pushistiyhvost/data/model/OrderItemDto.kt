package com.example.pushistiyhvost.data.model

data class OrderItemDto(
    val productId: String = "",
    val name: String = "",
    val price: Int = 0,
    val quantity: Int = 0
)