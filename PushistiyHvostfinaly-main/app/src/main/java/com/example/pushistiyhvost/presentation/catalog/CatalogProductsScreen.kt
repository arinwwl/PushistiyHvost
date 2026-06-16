package com.example.pushistiyhvost.presentation.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.pushistiyhvost.data.repository.ProductRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.GetProductsUseCase
import com.example.pushistiyhvost.presentation.home.HomeViewModel
import com.example.pushistiyhvost.presentation.home.HomeViewModelFactory
import com.example.pushistiyhvost.ui.components.ProductCard
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun CatalogProductsScreen(
    categoryTitle: String,
    onProductClick: (String) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    val repository = ProductRepositoryImpl(firestore)
    val getProductsUseCase = GetProductsUseCase(repository)

    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(getProductsUseCase)
    )

    val allProducts by viewModel.products.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    val filteredProducts = allProducts.filter { product ->
        normalizeCategory(product.category) == normalizeCategory(categoryTitle)
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
                text = categoryTitle,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Товаров: ${filteredProducts.size}",
                color = Color.Gray
            )



            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product.id) }
                    )
                }
            }
        }
    }
}

private fun normalizeCategory(value: String): String {
    return value
        .trim()
        .replace("\"", "")
        .replace("«", "")
        .replace("»", "")
        .lowercase()
}