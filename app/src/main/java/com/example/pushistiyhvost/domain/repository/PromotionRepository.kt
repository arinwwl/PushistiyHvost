package com.example.pushistiyhvost.domain.repository

import com.example.pushistiyhvost.presentation.admin.model.Promotion

interface PromotionRepository {
    suspend fun getPromotions(): List<Promotion>
    suspend fun getPromotionById(promotionId: String): Promotion?
    suspend fun addPromotion(promotion: Promotion): Result<Unit>
    suspend fun updatePromotion(promotion: Promotion): Result<Unit>
}