package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.OrderRepository

class GetOrdersUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(userId: String) = repository.getOrders(userId)
}
