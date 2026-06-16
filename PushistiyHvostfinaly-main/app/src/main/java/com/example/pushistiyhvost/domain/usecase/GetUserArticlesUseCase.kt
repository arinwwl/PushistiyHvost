package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.UserArticleRepository

class GetUserArticlesUseCase(
    private val repository: UserArticleRepository
) {
    suspend operator fun invoke() = repository.getArticles()
}