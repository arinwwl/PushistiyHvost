package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.PromotionRepository

class GetPromotionByIdUseCase(
    private val repository: PromotionRepository
) {
    suspend operator fun invoke(promotionId: String) = repository.getPromotionById(promotionId)
}