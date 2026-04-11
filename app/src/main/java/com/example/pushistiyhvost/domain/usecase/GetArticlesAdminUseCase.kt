package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.ArticleRepository

class GetArticlesAdminUseCase(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke() = repository.getArticles()
}