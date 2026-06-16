package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.PromotionRepository
import com.example.pushistiyhvost.presentation.admin.model.Promotion

class AddPromotionUseCase(
    private val repository: PromotionRepository
) {
    suspend operator fun invoke(promotion: Promotion): Result<Unit> {
        return repository.addPromotion(promotion)
    }
}