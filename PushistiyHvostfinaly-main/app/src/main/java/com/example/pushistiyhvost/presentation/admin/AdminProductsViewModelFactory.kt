package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.AddProductUseCase
import com.example.pushistiyhvost.domain.usecase.GetProductByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetProductsUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateProductUseCase

class AdminProductsViewModelFactory(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminProductsViewModel::class.java)) {
            return AdminProductsViewModel(
                getProductsUseCase = getProductsUseCase,
                getProductByIdUseCase = getProductByIdUseCase,
                addProductUseCase = addProductUseCase,
                updateProductUseCase = updateProductUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}