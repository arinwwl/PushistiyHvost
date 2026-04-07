package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.OrderRepository
import com.example.pushistiyhvost.presentation.cart.model.CartItem

class CreateOrderUseCase(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(
        userId: String,
        items: List<CartItem>,
        totalPrice: Int,
        deliveryType: String,
        address: String,
        date: String,
        time: String,
        paymentType: String
    ) {
        repository.createOrder(
            userId = userId,
            items = items,
            totalPrice = totalPrice,
            deliveryType = deliveryType,
            address = address,
            date = date,
            time = time,
            paymentType = paymentType
        )
    }
}