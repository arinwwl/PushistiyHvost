package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.GetAdminStatisticsUseCase

class AdminStatisticsViewModelFactory(
    private val getAdminStatisticsUseCase: GetAdminStatisticsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminStatisticsViewModel::class.java)) {
            return AdminStatisticsViewModel(
                getAdminStatisticsUseCase = getAdminStatisticsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}