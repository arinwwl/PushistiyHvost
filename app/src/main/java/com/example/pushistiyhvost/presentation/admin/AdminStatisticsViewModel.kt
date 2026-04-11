package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.GetAdminStatisticsUseCase
import com.example.pushistiyhvost.presentation.admin.model.AdminStatistics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AdminStatisticsUiState {
    data object Idle : AdminStatisticsUiState()
    data object Loading : AdminStatisticsUiState()
    data class Success(val statistics: AdminStatistics) : AdminStatisticsUiState()
    data class Error(val message: String) : AdminStatisticsUiState()
}

class AdminStatisticsViewModel(
    private val getAdminStatisticsUseCase: GetAdminStatisticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminStatisticsUiState>(AdminStatisticsUiState.Idle)
    val uiState: StateFlow<AdminStatisticsUiState> = _uiState

    fun loadStatistics() {
        viewModelScope.launch {
            _uiState.value = AdminStatisticsUiState.Loading
            try {
                val statistics = getAdminStatisticsUseCase()
                _uiState.value = AdminStatisticsUiState.Success(statistics)
            } catch (e: Exception) {
                _uiState.value = AdminStatisticsUiState.Error(
                    e.message ?: "Ошибка загрузки статистики"
                )
            }
        }
    }
}