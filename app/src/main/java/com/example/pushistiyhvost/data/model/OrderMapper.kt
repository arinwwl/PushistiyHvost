package com.example.pushistiyhvost.data.model

import com.example.pushistiyhvost.presentation.orders.model.Order

fun OrderDto.toOrder(): Order {
    return Order(
        id = id,
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