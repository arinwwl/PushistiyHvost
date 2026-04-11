package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.OrderRepository

class GetOrderByIdAdminUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(orderId: String) = repository.getOrderById(orderId)
}