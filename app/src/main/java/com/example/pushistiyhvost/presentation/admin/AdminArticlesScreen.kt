package com.example.pushistiyhvost.presentation.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun AdminArticlesScreen(
    onAddArticleClick: () -> Unit,
    onEditArticleClick: (String) -> Unit
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

    val articles by viewModel.articles.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val search = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadArticles()
    }

    val filteredArticles = articles.filter { article ->
        article.title.contains(search.value, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Статьи",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = search.value,
            onValueChange = { search.value = it },
            label = { Text("Поиск") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onAddArticleClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить статью")
        }

        when (val currentState = uiState) {
            AdminArticlesUiState.Idle -> Unit

            AdminArticlesUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminArticlesUiState.Success -> {
                Text(currentState.message)
            }

            is AdminArticlesUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredArticles) { article ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEditArticleClick(article.id) },
                    colors = CardDefaults.cardColors()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = article.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = article.text.take(120),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}