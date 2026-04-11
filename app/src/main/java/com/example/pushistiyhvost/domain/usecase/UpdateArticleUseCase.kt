package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.ArticleRepository
import com.example.pushistiyhvost.presentation.admin.model.Article

class UpdateArticleUseCase(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(article: Article): Result<Unit> {
        return repository.updateArticle(article)
    }
}