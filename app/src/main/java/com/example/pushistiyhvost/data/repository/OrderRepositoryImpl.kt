package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.data.model.OrderDto
import com.example.pushistiyhvost.data.model.OrderItemDto
import com.example.pushistiyhvost.data.model.toOrder
import com.example.pushistiyhvost.domain.repository.OrderRepository
import com.example.pushistiyhvost.presentation.cart.model.CartItem
import com.example.pushistiyhvost.presentation.orders.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class OrderRepositoryImpl(
    private val firestore: FirebaseFirestore
) : OrderRepository {

    override suspend fun createOrder(
        userId: String,
        items: List<CartItem>,
        totalPrice: Int,
        deliveryType: String,
        address: String,
        date: String,
        time: String,
        paymentType: String
    ) {
        val orderId = firestore.collection("orders").document().id

        val orderDto = OrderDto(
            id = orderId,
            userId = userId,
            items = items.map {
                OrderItemDto(
                    productId = it.product.id,
                    name = it.product.name,
                    price = it.product.price,
                    quantity = it.quantity
                )
            },
            totalPrice = totalPrice,
            status = "Создан",
            createdAt = System.currentTimeMillis(),
            deliveryType = deliveryType,
            address = address,
            date = date,
            time = time,
            paymentType = paymentType
        )

        firestore.collection("orders")
            .document(orderId)
            .set(orderDto)
            .await()
    }

    override suspend fun getOrders(userId: String): List<Order> {
        return try {
            val snapshot = firestore.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(OrderDto::class.java)
                    ?.copy(id = document.id)
                    ?.toOrder()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}