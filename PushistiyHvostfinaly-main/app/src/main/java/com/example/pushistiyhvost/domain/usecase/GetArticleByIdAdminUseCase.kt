package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.ArticleRepository

class GetArticleByIdAdminUseCase(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(articleId: String) = repository.getArticleById(articleId)
}