package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.PromotionRepository

class GetPromotionsUseCase(
    private val repository: PromotionRepository
) {
    suspend operator fun invoke() = repository.getPromotions()
}