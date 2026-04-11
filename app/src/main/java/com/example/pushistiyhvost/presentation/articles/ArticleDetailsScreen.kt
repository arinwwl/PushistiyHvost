package com.example.pushistiyhvost.presentation.articles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
fun ArticleDetailsScreen(
    articleId: String
) {
    val firestore = FirebaseFirestore.getInstance()
    val repository = UserArticleRepositoryImpl(firestore)

    val viewModel: ArticlesViewModel = viewModel(
        factory = ArticlesViewModelFactory(
            getUserArticlesUseCase = GetUserArticlesUseCase(repository),
            getUserArticleByIdUseCase = GetUserArticleByIdUseCase(repository)
        )
    )

    val article by viewModel.selectedArticle.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(articleId) {
        viewModel.loadArticleById(articleId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val state = uiState) {
            ArticlesUiState.Loading -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    CircularProgressIndicator()
                }
            }

            is ArticlesUiState.Error -> {
                Text(
                    text = state.message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }

            ArticlesUiState.Idle -> {
                if (article == null) {
                    Text(
                        text = "Статья не найдена",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            color = Color.Transparent
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFFEADFFF), Color(0xFFF5F1FA))
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Text(
                                    text = "Полезная статья",
                                    color = Color(0xFF6F4AE6),
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = article!!.title,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2B2740)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = article!!.text,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}