package com.example.pushistiyhvost.presentation.home.model

data class Product(
    val id: String,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val rating: Double,
    val description: String,
    val category: String,
    val characteristics: List<String>,
    val reviews: List<String>
)