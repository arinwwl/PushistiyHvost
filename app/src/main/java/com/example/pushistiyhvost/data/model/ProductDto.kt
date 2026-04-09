package com.example.pushistiyhvost.data.model

data class ProductDto(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val imageBase64: String = "",
    val rating: Double = 0.0,
    val description: String = "",
    val category: String = "",
    val characteristics: List<String> = emptyList(),
    val reviews: List<String> = emptyList()
)