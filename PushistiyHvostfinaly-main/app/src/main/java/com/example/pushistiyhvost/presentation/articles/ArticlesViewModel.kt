package com.example.pushistiyhvost.presentation.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.GetUserArticleByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetUserArticlesUseCase
import com.example.pushistiyhvost.presentation.articles.model.ArticleUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ArticlesUiState {
    data object Idle : ArticlesUiState()
    data object Loading : ArticlesUiState()
    data class Error(val message: String) : ArticlesUiState()
}

class ArticlesViewModel(
    private val getUserArticlesUseCase: GetUserArticlesUseCase,
    private val getUserArticleByIdUseCase: GetUserArticleByIdUseCase
) : ViewModel() {

    private val _articles = MutableStateFlow<List<ArticleUi>>(emptyList())
    val articles: StateFlow<List<ArticleUi>> = _articles

    private val _selectedArticle = MutableStateFlow<ArticleUi?>(null)
    val selectedArticle: StateFlow<ArticleUi?> = _selectedArticle

    private val _uiState = MutableStateFlow<ArticlesUiState>(ArticlesUiState.Idle)
    val uiState: StateFlow<ArticlesUiState> = _uiState

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = ArticlesUiState.Loading
            try {
                _articles.value = getUserArticlesUseCase()
                _uiState.value = ArticlesUiState.Idle
            } catch (e: Exception) {
                _uiState.value = ArticlesUiState.Error(
                    e.message ?: "Ошибка загрузки статей"
                )
            }
        }
    }

    fun loadArticleById(articleId: String) {
        viewModelScope.launch {
            _uiState.value = ArticlesUiState.Loading
            try {
                _selectedArticle.value = getUserArticleByIdUseCase(articleId)
                _uiState.value = ArticlesUiState.Idle
            } catch (e: Exception) {
                _uiState.value = ArticlesUiState.Error(
                    e.message ?: "Ошибка загрузки статьи"
                )
            }
        }
    }
}