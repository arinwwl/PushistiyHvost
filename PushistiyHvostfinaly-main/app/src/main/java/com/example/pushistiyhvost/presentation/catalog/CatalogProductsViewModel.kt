package com.example.pushistiyhvost.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.GetProductsByCategoryUseCase
import com.example.pushistiyhvost.presentation.home.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogProductsViewModel(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    fun loadProducts(category: String) {
        viewModelScope.launch {
            _products.value = getProductsByCategoryUseCase(category)
        }
    }
}