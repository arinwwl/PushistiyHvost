package com.example.pushistiyhvost.data.model

import com.example.pushistiyhvost.presentation.orders.model.Order
import com.example.pushistiyhvost.presentation.orders.model.OrderItem

fun OrderDto.toOrder(): Order {
    return Order(
        id = id,
        userId = userId,
        items = items.map { item ->
            OrderItem(
                productId = item.productId,
                name = item.name,
                price = item.price,
                quantity = item.quantity
            )
        },
        totalPrice = totalPrice,
        status = status,
        createdAt = createdAt,
        deliveryType = deliveryType,
        address = address,
        date = date,
        time = time,
        paymentType = paymentType
    )
}