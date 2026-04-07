package com.example.pushistiyhvost.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.GetOrdersUseCase

class OrdersViewModelFactory(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrdersViewModel::class.java)) {
            return OrdersViewModel(getOrdersUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}