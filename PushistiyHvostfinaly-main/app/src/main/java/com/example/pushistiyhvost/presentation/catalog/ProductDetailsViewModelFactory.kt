package com.example.pushistiyhvost.presentation.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.GetProductByIdUseCase

class ProductDetailsViewModelFactory(
    private val getProductByIdUseCase: GetProductByIdUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel(getProductByIdUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}