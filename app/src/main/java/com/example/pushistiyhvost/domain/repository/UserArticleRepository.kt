package com.example.pushistiyhvost.domain.repository

import com.example.pushistiyhvost.presentation.articles.model.ArticleUi

interface UserArticleRepository {
    suspend fun getArticles(): List<ArticleUi>
    suspend fun getArticleById(articleId: String): ArticleUi?
}