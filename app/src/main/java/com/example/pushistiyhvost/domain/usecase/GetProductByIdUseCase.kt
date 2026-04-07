package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.ProductRepository
import com.example.pushistiyhvost.presentation.home.model.Product

class GetProductByIdUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String): Product? {
        return repository.getProductById(productId)
    }
}