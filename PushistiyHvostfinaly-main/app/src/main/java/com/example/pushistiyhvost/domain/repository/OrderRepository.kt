package com.example.pushistiyhvost.domain.repository

import com.example.pushistiyhvost.presentation.cart.model.CartItem
import com.example.pushistiyhvost.presentation.orders.model.Order

interface OrderRepository {
    suspend fun createOrder(
        userId: String,
        items: List<CartItem>,
        totalPrice: Int,
        deliveryType: String,
        address: String,
        date: String,
        time: String,
        paymentType: String
    )

    suspend fun getOrders(userId: String): List<Order>

    suspend fun getAllOrders(): List<Order>
    suspend fun getOrderById(orderId: String): Order?
    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit>
}