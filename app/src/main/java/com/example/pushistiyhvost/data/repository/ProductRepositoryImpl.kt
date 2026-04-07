package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.data.model.ProductDto
import com.example.pushistiyhvost.data.model.toProduct
import com.example.pushistiyhvost.domain.repository.ProductRepository
import com.example.pushistiyhvost.presentation.home.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl(
    private val firestore: FirebaseFirestore
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        return try {
            val snapshot = firestore.collection("products").get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(ProductDto::class.java)
                    ?.copy(id = document.id)
                    ?.toProduct()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getProductsByCategory(category: String): List<Product> {
        return try {
            val snapshot = firestore.collection("products").get().await()

            snapshot.documents.mapNotNull { document ->
                val dto = document.toObject(ProductDto::class.java) ?: return@mapNotNull null

                dto.copy(id = document.id)
            }
                .filter {
                    it.category?.trim()?.lowercase() ==
                            category.trim().lowercase()
                }
                .map { it.toProduct() }

        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getProductById(productId: String): Product? {
        return try {
            val document = firestore.collection("products")
                .document(productId)
                .get()
                .await()

            document.toObject(ProductDto::class.java)
                ?.copy(id = document.id)
                ?.toProduct()
        } catch (e: Exception) {
            null
        }
    }
}