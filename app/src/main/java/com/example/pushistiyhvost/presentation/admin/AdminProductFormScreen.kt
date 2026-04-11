package com.example.pushistiyhvost.presentation.admin

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pushistiyhvost.data.repository.ProductRepositoryImpl
import com.example.pushistiyhvost.domain.usecase.AddProductUseCase
import com.example.pushistiyhvost.domain.usecase.GetProductByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetProductsUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateProductUseCase
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdminProductFormScreen(
    productId: String?,
    onSaved: () -> Unit
) {
    val context = LocalContext.current
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

    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageBase64 by remember { mutableStateOf("") }
    var characteristicsText by remember { mutableStateOf("") }
    var inStock by remember { mutableStateOf(true) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val base64 = uriToBase64(context, uri)
            if (base64 != null) {
                imageBase64 = base64
            }
        }
    }

    LaunchedEffect(productId) {
        if (!productId.isNullOrBlank()) {
            viewModel.loadProductById(productId)
        } else {
            viewModel.clearSelectedProduct()
        }
    }

    LaunchedEffect(selectedProduct) {
        selectedProduct?.let { product ->
            name = product.name
            price = product.price.toString()
            description = product.description
            category = product.category
            imageBase64 = product.imageBase64
            characteristicsText = product.characteristics.joinToString("\n")
            inStock = product.inStock
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (productId == null) "Добавить товар" else "Редактировать товар",
            style = MaterialTheme.typography.headlineMedium
        )

        when (val currentState = uiState) {
            AdminProductsUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminProductsUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is AdminProductsUiState.Success -> {
                Text(currentState.message)
            }

            AdminProductsUiState.Idle -> Unit
        }

        Text(
            text = "Фото товара",
            style = MaterialTheme.typography.titleMedium
        )

        if (imageBase64.isNotBlank()) {
            val bitmap = decodeBase64ToBitmap(imageBase64)

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Фото товара",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        },
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable {
                            imagePickerLauncher.launch("image/*")
                        },
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFE8E5F2)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Нажмите, чтобы выбрать фото")
                    }
                }
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clickable {
                        imagePickerLauncher.launch("image/*")
                    },
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE8E5F2)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Нажмите, чтобы выбрать фото")
                }
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Цена") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Категория") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = characteristicsText,
            onValueChange = { characteristicsText = it },
            label = { Text("Характеристики (каждая с новой строки)") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("В наличии")
            Switch(
                checked = inStock,
                onCheckedChange = { inStock = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.saveProduct(
                    productId = productId,
                    name = name,
                    price = price,
                    description = description,
                    category = category,
                    imageBase64 = imageBase64,
                    characteristicsText = characteristicsText,
                    inStock = inStock,
                    onSuccess = onSaved
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить")
        }
    }
}

private fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val originalBytes = inputStream.readBytes()
        inputStream.close()

        val bitmap = BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size)
            ?: return null

        val outputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 40, outputStream)

        val compressedBytes = outputStream.toByteArray()
        Base64.encodeToString(compressedBytes, Base64.DEFAULT)
    } catch (e: Exception) {
        null
    }
}

private fun decodeBase64ToBitmap(base64: String) =
    try {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }