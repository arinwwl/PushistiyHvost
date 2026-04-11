package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.domain.repository.UserArticleRepository
import com.example.pushistiyhvost.presentation.articles.model.ArticleUi
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserArticleRepositoryImpl(
    private val firestore: FirebaseFirestore
) : UserArticleRepository {

    override suspend fun getArticles(): List<ArticleUi> {
        return try {
            val snapshot = firestore.collection("articles").get().await()

            snapshot.documents.map { document ->
                ArticleUi(
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

    override suspend fun getArticleById(articleId: String): ArticleUi? {
        return try {
            val document = firestore.collection("articles")
                .document(articleId)
                .get()
                .await()

            if (!document.exists()) return null

            ArticleUi(
                id = document.id,
                title = document.getString("title") ?: "",
                text = document.getString("text") ?: "",
                image = document.getString("image") ?: ""
            )
        } catch (e: Exception) {
            null
        }
    }
}