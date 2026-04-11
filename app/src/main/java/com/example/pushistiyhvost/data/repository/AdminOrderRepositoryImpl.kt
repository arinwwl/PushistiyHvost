package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.domain.repository.AdminOrderRepository
import com.example.pushistiyhvost.presentation.admin.model.AdminOrder
import com.example.pushistiyhvost.presentation.admin.model.AdminOrderItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminOrderRepositoryImpl(
    private val firestore: FirebaseFirestore
) : AdminOrderRepository {

    override suspend fun getOrders(): List<AdminOrder> {
        val snapshot = firestore.collection("orders").get().await()

        return snapshot.documents.map { doc ->
            AdminOrder(
                id = doc.id,
                userId = doc.getString("userId") ?: "",
                address = doc.getString("address") ?: "",
                deliveryType = doc.getString("deliveryType") ?: "",
                date = doc.getString("date") ?: "",
                time = doc.getString("time") ?: "",
                paymentType = doc.getString("paymentType") ?: "",
                status = doc.getString("status") ?: "",
                totalPrice = (doc.getLong("totalPrice") ?: 0L).toInt(),
                items = (doc.get("items") as? List<*>)?.map {
                    val map = it as Map<*, *>
                    AdminOrderItem(
                        name = map["name"] as? String ?: "",
                        price = (map["price"] as? Long ?: 0L).toInt(),
                        quantity = (map["quantity"] as? Long ?: 0L).toInt()
                    )
                } ?: emptyList()
            )
        }
    }

    override suspend fun getOrderById(id: String): AdminOrder? {
        val doc = firestore.collection("orders").document(id).get().await()

        if (!doc.exists()) return null

        return AdminOrder(
            id = doc.id,
            userId = doc.getString("userId") ?: "",
            address = doc.getString("address") ?: "",
            deliveryType = doc.getString("deliveryType") ?: "",
            date = doc.getString("date") ?: "",
            time = doc.getString("time") ?: "",
            paymentType = doc.getString("paymentType") ?: "",
            status = doc.getString("status") ?: "",
            totalPrice = (doc.getLong("totalPrice") ?: 0L).toInt(),
            items = (doc.get("items") as? List<*>)?.map {
                val map = it as Map<*, *>
                AdminOrderItem(
                    name = map["name"] as? String ?: "",
                    price = (map["price"] as? Long ?: 0L).toInt(),
                    quantity = (map["quantity"] as? Long ?: 0L).toInt()
                )
            } ?: emptyList()
        )
    }

    override suspend fun updateStatus(orderId: String, status: String): Result<Unit> {
        return try {
            firestore.collection("orders")
                .document(orderId)
                .update("status", status)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}