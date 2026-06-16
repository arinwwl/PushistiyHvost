package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.ProductRepository

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke() = repository.getProducts()
}