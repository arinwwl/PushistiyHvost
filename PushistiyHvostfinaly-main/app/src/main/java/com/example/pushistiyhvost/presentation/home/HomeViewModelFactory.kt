package com.example.pushistiyhvost.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.GetProductsUseCase

class HomeViewModelFactory(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(getProductsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}