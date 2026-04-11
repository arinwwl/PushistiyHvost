package com.example.pushistiyhvost.presentation.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.GetUserArticleByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetUserArticlesUseCase

class ArticlesViewModelFactory(
    private val getUserArticlesUseCase: GetUserArticlesUseCase,
    private val getUserArticleByIdUseCase: GetUserArticleByIdUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticlesViewModel::class.java)) {
            return ArticlesViewModel(
                getUserArticlesUseCase = getUserArticlesUseCase,
                getUserArticleByIdUseCase = getUserArticleByIdUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}