package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.GetAllOrdersUseCase
import com.example.pushistiyhvost.domain.usecase.GetOrderByIdAdminUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateOrderStatusUseCase

class AdminOrdersViewModelFactory(
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val getOrderByIdAdminUseCase: GetOrderByIdAdminUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminOrdersViewModel::class.java)) {
            return AdminOrdersViewModel(
                getAllOrdersUseCase = getAllOrdersUseCase,
                getOrderByIdAdminUseCase = getOrderByIdAdminUseCase,
                updateOrderStatusUseCase = updateOrderStatusUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}