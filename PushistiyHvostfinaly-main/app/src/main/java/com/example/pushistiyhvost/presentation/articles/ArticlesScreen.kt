package com.example.pushistiyhvost.presentation.articles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.UserArticleRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.GetUserArticleByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetUserArticlesUseCase
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ArticlesScreen(
    onArticleClick: (String) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    val repository = UserArticleRepositoryImpl(firestore)

    val viewModel: ArticlesViewModel = viewModel(
        factory = ArticlesViewModelFactory(
            getUserArticlesUseCase = GetUserArticlesUseCase(repository),
            getUserArticleByIdUseCase = GetUserArticleByIdUseCase(repository)
        )
    )

    val articles by viewModel.articles.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadArticles()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Статьи",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (val state = uiState) {
                ArticlesUiState.Idle -> Unit
                ArticlesUiState.Loading -> CircularProgressIndicator()
                is ArticlesUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(articles) { article ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onArticleClick(article.id) },
                        color = Color.White,
                        tonalElevation = 1.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Полезная статья",
                                color = Color(0xFF6F4AE6),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = article.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = article.text.take(120),
                                color = Color(0xFF6C6880)
                            )
                        }
                    }
                }
            }
        }
    }
}