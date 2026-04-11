package com.example.pushistiyhvost.data.model

import com.example.pushistiyhvost.presentation.home.model.Product

fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        name = name,
        price = price,
        imageBase64 = imageBase64,
        rating = rating,
        description = description,
        category = category,
        characteristics = characteristics,
        reviews = reviews,
        inStock = inStock
    )
}