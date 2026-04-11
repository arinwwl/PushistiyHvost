package com.example.pushistiyhvost.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.ArticleRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.AddArticleUseCase
import com.example.pushistiyhvost.domain.usecase.GetArticleByIdAdminUseCase
import com.example.pushistiyhvost.domain.usecase.GetArticlesAdminUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateArticleUseCase
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminArticleFormScreen(
    articleId: String?,
    onSaved: () -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    val repository = ArticleRepositoryImpl(firestore)

    val viewModel: AdminArticlesViewModel = viewModel(
        factory = AdminArticlesViewModelFactory(
            getArticlesAdminUseCase = GetArticlesAdminUseCase(repository),
            getArticleByIdAdminUseCase = GetArticleByIdAdminUseCase(repository),
            addArticleUseCase = AddArticleUseCase(repository),
            updateArticleUseCase = UpdateArticleUseCase(repository)
        )
    )

    val selectedArticle by viewModel.selectedArticle.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }

    LaunchedEffect(articleId) {
        if (!articleId.isNullOrBlank()) {
            viewModel.loadArticleById(articleId)
        } else {
            viewModel.clearSelectedArticle()
        }
    }

    LaunchedEffect(selectedArticle) {
        selectedArticle?.let { article ->
            title = article.title
            text = article.text
            image = article.image
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (articleId == null) "Добавить статью" else "Редактировать статью",
            style = MaterialTheme.typography.headlineMedium
        )

        when (val currentState = uiState) {
            AdminArticlesUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminArticlesUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is AdminArticlesUiState.Success -> {
                Text(currentState.message)
            }

            AdminArticlesUiState.Idle -> Unit
        }

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Заголовок") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Текст") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = image,
            onValueChange = { image = it },
            label = { Text("Картинка") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.saveArticle(
                    articleId = articleId,
                    title = title,
                    text = text,
                    image = image,
                    onSuccess = onSaved
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
}