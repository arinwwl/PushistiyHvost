package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.ProductRepository
import com.example.pushistiyhvost.presentation.home.model.Product

class AddProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Result<Unit> {
        return repository.addProduct(product)
    }
}