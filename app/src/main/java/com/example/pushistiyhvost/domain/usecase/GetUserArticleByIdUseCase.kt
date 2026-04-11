package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.UserArticleRepository

class GetUserArticleByIdUseCase(
    private val repository: UserArticleRepository
) {
    suspend operator fun invoke(articleId: String) = repository.getArticleById(articleId)
}