package com.example.pushistiyhvost.presentation.cart.model

import com.example.pushistiyhvost.presentation.home.model.Product

data class CartItem(
    val product: Product,
    val quantity: Int
)