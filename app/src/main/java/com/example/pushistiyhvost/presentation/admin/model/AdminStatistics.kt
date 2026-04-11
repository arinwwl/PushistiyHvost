package com.example.pushistiyhvost.presentation.admin.model

data class AdminStatistics(
    val totalOrders: Int,
    val totalRevenue: Int,
    val popularProducts: List<PopularProduct>
)

data class PopularProduct(
    val name: String,
    val count: Int
)