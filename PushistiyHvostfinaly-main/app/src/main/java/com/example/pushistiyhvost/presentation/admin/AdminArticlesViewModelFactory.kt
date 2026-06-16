package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.AddArticleUseCase
import com.example.pushistiyhvost.domain.usecase.GetArticleByIdAdminUseCase
import com.example.pushistiyhvost.domain.usecase.GetArticlesAdminUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateArticleUseCase

class AdminArticlesViewModelFactory(
    private val getArticlesAdminUseCase: GetArticlesAdminUseCase,
    private val getArticleByIdAdminUseCase: GetArticleByIdAdminUseCase,
    private val addArticleUseCase: AddArticleUseCase,
    private val updateArticleUseCase: UpdateArticleUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminArticlesViewModel::class.java)) {
            return AdminArticlesViewModel(
                getArticlesAdminUseCase = getArticlesAdminUseCase,
                getArticleByIdAdminUseCase = getArticleByIdAdminUseCase,
                addArticleUseCase = addArticleUseCase,
                updateArticleUseCase = updateArticleUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}