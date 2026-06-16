package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.ProductRepository
import com.example.pushistiyhvost.presentation.home.model.Product

class GetProductsByCategoryUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(category: String): List<Product> {
        return repository.getProductsByCategory(category)
    }
}