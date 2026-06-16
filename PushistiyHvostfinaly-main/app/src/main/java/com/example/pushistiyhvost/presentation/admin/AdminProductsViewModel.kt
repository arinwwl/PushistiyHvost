package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.AddProductUseCase
import com.example.pushistiyhvost.domain.usecase.GetProductByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetProductsUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateProductUseCase
import com.example.pushistiyhvost.presentation.home.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AdminProductsUiState {
    data object Idle : AdminProductsUiState()
    data object Loading : AdminProductsUiState()
    data class Success(val message: String) : AdminProductsUiState()
    data class Error(val message: String) : AdminProductsUiState()
}

class AdminProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct

    private val _uiState = MutableStateFlow<AdminProductsUiState>(AdminProductsUiState.Idle)
    val uiState: StateFlow<AdminProductsUiState> = _uiState

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = AdminProductsUiState.Loading
            try {
                _products.value = getProductsUseCase()
                _uiState.value = AdminProductsUiState.Idle
            } catch (e: Exception) {
                _uiState.value = AdminProductsUiState.Error(
                    e.message ?: "Ошибка загрузки товаров"
                )
            }
        }
    }

    fun loadProductById(productId: String) {
        viewModelScope.launch {
            _uiState.value = AdminProductsUiState.Loading
            try {
                _selectedProduct.value = getProductByIdUseCase(productId)
                _uiState.value = AdminProductsUiState.Idle
            } catch (e: Exception) {
                _uiState.value = AdminProductsUiState.Error(
                    e.message ?: "Ошибка загрузки товара"
                )
            }
        }
    }

    fun clearSelectedProduct() {
        _selectedProduct.value = null
    }

    fun saveProduct(
        productId: String?,
        name: String,
        price: String,
        description: String,
        category: String,
        imageBase64: String,
        characteristicsText: String,
        inStock: Boolean,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = AdminProductsUiState.Loading

            val priceValue = price.toIntOrNull()
            if (name.isBlank() || priceValue == null || category.isBlank()) {
                _uiState.value = AdminProductsUiState.Error(
                    "Заполните название, цену и категорию"
                )
                return@launch
            }

            val characteristics = characteristicsText
                .split("\n")
                .map { it.trim() }
                .filter { it.isNotBlank() }

            val product = Product(
                id = productId ?: "",
                name = name.trim(),
                price = priceValue,
                imageBase64 = imageBase64.trim(),
                rating = selectedProduct.value?.rating ?: 0.0,
                description = description.trim(),
                category = category.trim(),
                characteristics = characteristics,
                reviews = selectedProduct.value?.reviews ?: emptyList(),
                inStock = inStock
            )

            val result = if (productId.isNullOrBlank()) {
                addProductUseCase(product)
            } else {
                updateProductUseCase(product)
            }

            result.fold(
                onSuccess = {
                    _uiState.value = AdminProductsUiState.Success("Товар сохранён")
                    loadProducts()
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.value = AdminProductsUiState.Error(
                        error.message ?: "Ошибка сохранения товара"
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = AdminProductsUiState.Idle
    }
}