package com.example.pushistiyhvost.domain.repository

import com.example.pushistiyhvost.presentation.admin.model.AdminOrder

interface AdminOrderRepository {
    suspend fun getOrders(): List<AdminOrder>
    suspend fun getOrderById(id: String): AdminOrder?
    suspend fun updateStatus(orderId: String, status: String): Result<Unit>
}