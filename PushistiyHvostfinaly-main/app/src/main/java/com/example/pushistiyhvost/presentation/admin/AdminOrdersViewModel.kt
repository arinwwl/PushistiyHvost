package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.GetAllOrdersUseCase
import com.example.pushistiyhvost.domain.usecase.GetOrderByIdAdminUseCase
import com.example.pushistiyhvost.domain.usecase.UpdateOrderStatusUseCase
import com.example.pushistiyhvost.presentation.orders.model.Order
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AdminOrdersUiState {
    data object Idle : AdminOrdersUiState()
    data object Loading : AdminOrdersUiState()
    data class Error(val message: String) : AdminOrdersUiState()
    data class Success(val message: String) : AdminOrdersUiState()
}

class AdminOrdersViewModel(
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val getOrderByIdAdminUseCase: GetOrderByIdAdminUseCase,
    private val updateOrderStatusUseCase: UpdateOrderStatusUseCase
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _selectedOrder = MutableStateFlow<Order?>(null)
    val selectedOrder: StateFlow<Order?> = _selectedOrder

    private val _uiState = MutableStateFlow<AdminOrdersUiState>(AdminOrdersUiState.Idle)
    val uiState: StateFlow<AdminOrdersUiState> = _uiState

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = AdminOrdersUiState.Loading
            try {
                _orders.value = getAllOrdersUseCase()
                _uiState.value = AdminOrdersUiState.Idle
            } catch (e: Exception) {
                _uiState.value = AdminOrdersUiState.Error(
                    e.message ?: "Ошибка загрузки заказов"
                )
            }
        }
    }

    fun loadOrderById(orderId: String) {
        viewModelScope.launch {
            _uiState.value = AdminOrdersUiState.Loading
            try {
                _selectedOrder.value = getOrderByIdAdminUseCase(orderId)
                _uiState.value = AdminOrdersUiState.Idle
            } catch (e: Exception) {
                _uiState.value = AdminOrdersUiState.Error(
                    e.message ?: "Ошибка загрузки заказа"
                )
            }
        }
    }

    fun updateStatus(orderId: String, status: String) {
        viewModelScope.launch {
            _uiState.value = AdminOrdersUiState.Loading

            val result = updateOrderStatusUseCase(orderId, status)

            result.fold(
                onSuccess = {
                    _selectedOrder.value = getOrderByIdAdminUseCase(orderId)
                    _orders.value = getAllOrdersUseCase()
                    _uiState.value = AdminOrdersUiState.Success("Статус обновлён")
                },
                onFailure = { error ->
                    _uiState.value = AdminOrdersUiState.Error(
                        error.message ?: "Ошибка обновления статуса"
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = AdminOrdersUiState.Idle
    }
}