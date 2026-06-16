package com.example.pushistiyhvost.domain.repository

import com.example.pushistiyhvost.presentation.admin.model.Article

interface ArticleRepository {
    suspend fun getArticles(): List<Article>
    suspend fun getArticleById(articleId: String): Article?
    suspend fun addArticle(article: Article): Result<Unit>
    suspend fun updateArticle(article: Article): Result<Unit>
}