package com.example.pushistiyhvost.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.ProductRepositoryImpl
import com.example.pushistiyhvost.data.repository.UserArticleRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.GetProductsUseCase
import com.example.pushistiyhvost.domain.usecase.GetUserArticleByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetUserArticlesUseCase
import com.example.pushistiyhvost.presentation.articles.ArticlesViewModel
import com.example.pushistiyhvost.presentation.articles.ArticlesViewModelFactory
import com.example.pushistiyhvost.ui.components.ProductCard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.example.pushistiyhvost.data.repository.ProfileRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.GetProfileImageUseCase
@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    onArticleClick: (String) -> Unit,
    onCatalogClick: () -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser
    val userName = user?.email
        ?.substringBefore("@")
        ?.replaceFirstChar { it.uppercase() }

    val displayName = userName ?: "друг"
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Все") }

    val categories = listOf("Все", "Кошки", "Собаки", "Птицы")

    val firestore = FirebaseFirestore.getInstance()
    val profileRepository = ProfileRepositoryImpl(firestore)
    val getProfileImageUseCase = GetProfileImageUseCase(profileRepository)

    var profileImageBase64 by remember { mutableStateOf<String?>(null) }

    val productRepository = ProductRepositoryImpl(firestore)
    val getProductsUseCase = GetProductsUseCase(productRepository)

    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(getProductsUseCase)
    )

    val articleRepository = UserArticleRepositoryImpl(firestore)
    val articlesViewModel: ArticlesViewModel = viewModel(
        factory = ArticlesViewModelFactory(
            getUserArticlesUseCase = GetUserArticlesUseCase(articleRepository),
            getUserArticleByIdUseCase = GetUserArticleByIdUseCase(articleRepository)
        )
    )

    val products by homeViewModel.products.collectAsState()
    val articles by articlesViewModel.articles.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.loadProducts()
        articlesViewModel.loadArticles()

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            profileImageBase64 = getProfileImageUseCase(userId)
        }
    }

    val filteredProducts = products.filter { product ->
        val matchesCategory = selectedCategory == "Все" ||
                product.category.equals(selectedCategory, ignoreCase = true)

        val matchesSearch = searchQuery.isBlank() ||
                product.name.contains(searchQuery.trim(), ignoreCase = true)

        matchesCategory && matchesSearch
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Привет, $displayName 👋",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                if (profileImageBase64 != null) {
                    val bitmap = decodeBase64ToBitmap(profileImageBase64!!)

                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Аватар",
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Surface(
                            modifier = Modifier.size(42.dp),
                            shape = CircleShape,
                            color = Color(0xFFD9D9D9)
                        ) {}
                    }
                } else {
                    Surface(
                        modifier = Modifier.size(42.dp),
                        shape = CircleShape,
                        color = Color(0xFFD9D9D9)
                    ) {}
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Найти товары для питомцев") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF6C542)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        IconButton(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Фильтры уже работают через поиск и категории",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Фильтр",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) Color(0xFFF6C542) else Color.White,
                        onClick = { selectedCategory = category }
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = Color(0xFF2B2740)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFEADFFF),
                                    Color(0xFFF5F1FA)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Text(
                            text = "Скидка 25%",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2B2740)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "на первый заказ",
                            fontSize = 18.sp,
                            color = Color(0xFF2B2740)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFF6C542),
                            modifier = Modifier.clickable { onCatalogClick() }
                        ) {
                            Text(
                                text = "Перейти в каталог",
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Вам и вашему питомцу",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Товаров найдено: ${filteredProducts.size}",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredProducts.isEmpty()) {
                Text(
                    text = "Нет товаров по вашему запросу",
                    color = Color(0xFF6C6880)
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            onClick = { onProductClick(product.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Полезные статьи",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (articles.isEmpty()) {
                Text(
                    text = "Статьи пока не добавлены",
                    color = Color(0xFF6C6880)
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(articles.take(3)) { article ->
                        Surface(
                            modifier = Modifier
                                .width(240.dp)
                                .clickable { onArticleClick(article.id) },
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White,
                            tonalElevation = 1.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Статья",
                                    color = Color(0xFF6F4AE6),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                Text(
                                    text = article.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF2B2740)
                                )

                                Text(
                                    text = article.text.take(100),
                                    color = Color(0xFF6C6880)
                                )
                            }

                        }
                    }
                }
            }
        }
    }

}
private fun decodeBase64ToBitmap(base64: String) =
    try {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }