package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.OrderRepository

class GetAllOrdersUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke() = repository.getAllOrders()
}