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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.ProductRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.AddProductUseCase
import com.example.pushistiyhvost.domain.usecase.GetProductByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetProductsUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateProductUseCase
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminProductsScreen(
    onAddProductClick: () -> Unit,
    onEditProductClick: (String) -> Unit
) {
    val firestore = FirebaseFirestore.getInstance()
    val repository = ProductRepositoryImpl(firestore)

    val viewModel: AdminProductsViewModel = viewModel(
        factory = AdminProductsViewModelFactory(
            getProductsUseCase = GetProductsUseCase(repository),
            getProductByIdUseCase = GetProductByIdUseCase(repository),
            addProductUseCase = AddProductUseCase(repository),
            updateProductUseCase = UpdateProductUseCase(repository)
        )
    )

    val products by viewModel.products.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val search = remember { mutableStateOf("") }

    val filteredProducts = products.filter { product ->
        product.name.contains(search.value, ignoreCase = true) ||
                product.category.contains(search.value, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Товары",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = search.value,
            onValueChange = { search.value = it },
            label = { Text("Поиск") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onAddProductClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Добавить товар")
        }

        when (val currentState = uiState) {
            AdminProductsUiState.Idle -> Unit

            AdminProductsUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminProductsUiState.Success -> {
                Text(currentState.message)
            }

            is AdminProductsUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredProducts) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEditProductClick(product.id) },
                    colors = CardDefaults.cardColors()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Цена: ${product.price} ₽")
                        Text("Категория: ${product.category}")

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Наличие:")
                            Text(if (product.inStock) "В наличии" else "Нет в наличии")
                        }
                    }
                }
            }
        }
    }
}