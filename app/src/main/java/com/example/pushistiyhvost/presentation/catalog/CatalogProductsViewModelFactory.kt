package com.example.pushistiyhvost.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.GetProductsByCategoryUseCase

class CatalogProductsViewModelFactory(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatalogProductsViewModel::class.java)) {
            return CatalogProductsViewModel(getProductsByCategoryUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}