package com.example.pushistiyhvost.data.model

import com.example.pushistiyhvost.presentation.home.model.Product

fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
        rating = rating,
        description = description,
        category = category,
        characteristics = characteristics,
        reviews = reviews
    )
}