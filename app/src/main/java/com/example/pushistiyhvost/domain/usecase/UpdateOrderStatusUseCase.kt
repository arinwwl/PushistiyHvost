package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.OrderRepository

class UpdateOrderStatusUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: String, status: String) =
        repository.updateOrderStatus(orderId, status)
}