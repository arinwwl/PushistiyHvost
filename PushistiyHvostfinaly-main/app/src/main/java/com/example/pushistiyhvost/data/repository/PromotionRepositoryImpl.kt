package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.domain.repository.PromotionRepository
import com.example.pushistiyhvost.presentation.admin.model.Promotion
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PromotionRepositoryImpl(
    private val firestore: FirebaseFirestore
) : PromotionRepository {

    override suspend fun getPromotions(): List<Promotion> {
        return try {
            val snapshot = firestore.collection("promotions").get().await()

            snapshot.documents.map { document ->
                Promotion(
                    id = document.id,
                    title = document.getString("title") ?: "",
                    discount = document.getString("discount") ?: "",
                    banner = document.getString("banner") ?: "",
                    date = document.getString("date") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPromotionById(promotionId: String): Promotion? {
        return try {
            val document = firestore.collection("promotions")
                .document(promotionId)
                .get()
                .await()

            if (!document.exists()) return null

            Promotion(
                id = document.id,
                title = document.getString("title") ?: "",
                discount = document.getString("discount") ?: "",
                banner = document.getString("banner") ?: "",
                date = document.getString("date") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addPromotion(promotion: Promotion): Result<Unit> {
        return try {
            val documentRef = if (promotion.id.isBlank()) {
                firestore.collection("promotions").document()
            } else {
                firestore.collection("promotions").document(promotion.id)
            }

            val promotionMap = hashMapOf(
                "title" to promotion.title,
                "discount" to promotion.discount,
                "banner" to promotion.banner,
                "date" to promotion.date
            )

            documentRef.set(promotionMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePromotion(promotion: Promotion): Result<Unit> {
        return try {
            if (promotion.id.isBlank()) {
                return Result.failure(Exception("Пустой id акции"))
            }

            val promotionMap = hashMapOf(
                "title" to promotion.title,
                "discount" to promotion.discount,
                "banner" to promotion.banner,
                "date" to promotion.date
            )

            firestore.collection("promotions")
                .document(promotion.id)
                .set(promotionMap)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}