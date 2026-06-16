package com.example.pushistiyhvost.data.repository

import android.util.Log
import com.example.pushistiyhvost.domain.repository.ProductRepository
import com.example.pushistiyhvost.presentation.home.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl(
    private val firestore: FirebaseFirestore
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        return try {
            val snapshot = firestore.collection("product").get().await()

            snapshot.documents.mapNotNull { document ->
                try {
                    Product(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        price = (document.getLong("price") ?: 0L).toInt(),
                        imageBase64 = document.getString("imageBase64") ?: "",
                        rating = document.getDouble("rating") ?: 0.0,
                        description = document.getString("description") ?: "",
                        category = document.getString("category") ?: "",
                        characteristics = (document.get("characteristics") as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
                        reviews = (document.get("reviews") as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
                        inStock = document.getBoolean("inStock") ?: true
                    )
                } catch (e: Exception) {
                    Log.e("PRODUCT_REPOSITORY", "Error parsing document ${document.id}: ${e.message}", e)
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("PRODUCT_REPOSITORY", "Error loading products: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getProductsByCategory(category: String): List<Product> {
        return getProducts().filter {
            it.category.trim().lowercase() == category.trim().lowercase()
        }
    }

    override suspend fun getProductById(productId: String): Product? {
        return try {
            val document = firestore.collection("product")
                .document(productId)
                .get()
                .await()

            if (!document.exists()) return null

            Product(
                id = document.id,
                name = document.getString("name") ?: "",
                price = (document.getLong("price") ?: 0L).toInt(),
                imageBase64 = document.getString("imageBase64") ?: "",
                rating = document.getDouble("rating") ?: 0.0,
                description = document.getString("description") ?: "",
                category = document.getString("category") ?: "",
                characteristics = (document.get("characteristics") as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
                reviews = (document.get("reviews") as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
                inStock = document.getBoolean("inStock") ?: true
            )
        } catch (e: Exception) {
            Log.e("PRODUCT_REPOSITORY", "Error loading product by id: ${e.message}", e)
            null
        }
    }

    override suspend fun addProduct(product: Product): Result<Unit> {
        return try {
            val documentRef = if (product.id.isBlank()) {
                firestore.collection("product").document()
            } else {
                firestore.collection("product").document(product.id)
            }

            val productMap = hashMapOf(
                "name" to product.name,
                "price" to product.price,
                "imageBase64" to product.imageBase64,
                "rating" to product.rating,
                "description" to product.description,
                "category" to product.category,
                "characteristics" to product.characteristics,
                "reviews" to product.reviews,
                "inStock" to product.inStock
            )

            documentRef.set(productMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("PRODUCT_REPOSITORY", "Error adding product: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun updateProduct(product: Product): Result<Unit> {
        return try {
            if (product.id.isBlank()) {
                return Result.failure(Exception("Пустой id товара"))
            }

            val productMap = hashMapOf(
                "name" to product.name,
                "price" to product.price,
                "imageBase64" to product.imageBase64,
                "rating" to product.rating,
                "description" to product.description,
                "category" to product.category,
                "characteristics" to product.characteristics,
                "reviews" to product.reviews,
                "inStock" to product.inStock
            )

            firestore.collection("product")
                .document(product.id)
                .set(productMap)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("PRODUCT_REPOSITORY", "Error updating product: ${e.message}", e)
            Result.failure(e)
        }
    }
}