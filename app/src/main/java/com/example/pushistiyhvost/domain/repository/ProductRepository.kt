package com.example.pushistiyhvost.domain.repository

import com.example.pushistiyhvost.presentation.home.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getProductsByCategory(category: String): List<Product>
    suspend fun getProductById(productId: String): Product?

    suspend fun addProduct(product: Product): Result<Unit>
    suspend fun updateProduct(product: Product): Result<Unit>
}