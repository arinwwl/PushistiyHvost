package com.example.pushistiyhvost.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.GetOrdersUseCase
import com.example.pushistiyhvost.presentation.orders.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val getOrdersUseCase: GetOrdersUseCase
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    fun loadOrders(userId: String) {
        viewModelScope.launch {
            _orders.value = getOrdersUseCase(userId)
        }
    }
}