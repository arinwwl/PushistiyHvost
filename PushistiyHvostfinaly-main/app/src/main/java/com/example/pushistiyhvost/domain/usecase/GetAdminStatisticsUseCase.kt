package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.OrderRepository
import com.example.pushistiyhvost.presentation.admin.model.AdminStatistics
import com.example.pushistiyhvost.presentation.admin.model.PopularProduct

class GetAdminStatisticsUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(): AdminStatistics {
        val orders = repository.getAllOrders()

        val totalOrders = orders.size
        val totalRevenue = orders.sumOf { it.totalPrice }

        val productCounts = mutableMapOf<String, Int>()

        orders.forEach { order ->
            order.items.forEach { item ->
                val current = productCounts[item.name] ?: 0
                productCounts[item.name] = current + item.quantity
            }
        }

        val popularProducts = productCounts
            .toList()
            .sortedByDescending { it.second }
            .take(5)
            .map { (name, count) ->
                PopularProduct(name = name, count = count)
            }

        return AdminStatistics(
            totalOrders = totalOrders,
            totalRevenue = totalRevenue,
            popularProducts = popularProducts
        )
    }
}