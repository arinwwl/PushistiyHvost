package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.AddArticleUseCase
import com.example.pushistiyhvost.domain.usecase.GetArticleByIdAdminUseCase
import com.example.pushistiyhvost.domain.usecase.GetArticlesAdminUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateArticleUseCase
import com.example.pushistiyhvost.presentation.admin.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AdminArticlesUiState {
    data object Idle : AdminArticlesUiState()
    data object Loading : AdminArticlesUiState()
    data class Success(val message: String) : AdminArticlesUiState()
    data class Error(val message: String) : AdminArticlesUiState()
}

class AdminArticlesViewModel(
    private val getArticlesAdminUseCase: GetArticlesAdminUseCase,
    private val getArticleByIdAdminUseCase: GetArticleByIdAdminUseCase,
    private val addArticleUseCase: AddArticleUseCase,
    private val updateArticleUseCase: UpdateArticleUseCase
) : ViewModel() {

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle

    private val _uiState = MutableStateFlow<AdminArticlesUiState>(AdminArticlesUiState.Idle)
    val uiState: StateFlow<AdminArticlesUiState> = _uiState

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = AdminArticlesUiState.Loading
            try {
                _articles.value = getArticlesAdminUseCase()
                _uiState.value = AdminArticlesUiState.Idle
            } catch (e: Exception) {
                _uiState.value = AdminArticlesUiState.Error(
                    e.message ?: "Ошибка загрузки статей"
                )
            }
        }
    }

    fun loadArticleById(articleId: String) {
        viewModelScope.launch {
            _uiState.value = AdminArticlesUiState.Loading
            try {
                _selectedArticle.value = getArticleByIdAdminUseCase(articleId)
                _uiState.value = AdminArticlesUiState.Idle
            } catch (e: Exception) {
                _uiState.value = AdminArticlesUiState.Error(
                    e.message ?: "Ошибка загрузки статьи"
                )
            }
        }
    }

    fun clearSelectedArticle() {
        _selectedArticle.value = null
    }

    fun saveArticle(
        articleId: String?,
        title: String,
        text: String,
        image: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = AdminArticlesUiState.Loading

            if (title.isBlank() || text.isBlank()) {
                _uiState.value = AdminArticlesUiState.Error(
                    "Заполните заголовок и текст статьи"
                )
                return@launch
            }

            val article = Article(
                id = articleId ?: "",
                title = title.trim(),
                text = text.trim(),
                image = image.trim()
            )

            val result = if (articleId.isNullOrBlank()) {
                addArticleUseCase(article)
            } else {
                updateArticleUseCase(article)
            }

            result.fold(
                onSuccess = {
                    _uiState.value = AdminArticlesUiState.Success("Статья сохранена")
                    loadArticles()
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.value = AdminArticlesUiState.Error(
                        error.message ?: "Ошибка сохранения статьи"
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = AdminArticlesUiState.Idle
    }
}