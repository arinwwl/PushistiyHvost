package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.domain.repository.ArticleRepository
import com.example.pushistiyhvost.presentation.admin.model.Article
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ArticleRepositoryImpl(
    private val firestore: FirebaseFirestore
) : ArticleRepository {

    override suspend fun getArticles(): List<Article> {
        return try {
            val snapshot = firestore.collection("articles").get().await()

            snapshot.documents.map { document ->
                Article(
                    id = document.id,
                    title = document.getString("title") ?: "",
                    text = document.getString("text") ?: "",
                    image = document.getString("image") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getArticleById(articleId: String): Article? {
        return try {
            val document = firestore.collection("articles")
                .document(articleId)
                .get()
                .await()

            if (!document.exists()) return null

            Article(
                id = document.id,
                title = document.getString("title") ?: "",
                text = document.getString("text") ?: "",
                image = document.getString("image") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addArticle(article: Article): Result<Unit> {
        return try {
            val documentRef = if (article.id.isBlank()) {
                firestore.collection("articles").document()
            } else {
                firestore.collection("articles").document(article.id)
            }

            val articleMap = hashMapOf(
                "title" to article.title,
                "text" to article.text,
                "image" to article.image
            )

            documentRef.set(articleMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateArticle(article: Article): Result<Unit> {
        return try {
            if (article.id.isBlank()) {
                return Result.failure(Exception("Пустой id статьи"))
            }

            val articleMap = hashMapOf(
                "title" to article.title,
                "text" to article.text,
                "image" to article.image
            )

            firestore.collection("articles")
                .document(article.id)
                .set(articleMap)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}