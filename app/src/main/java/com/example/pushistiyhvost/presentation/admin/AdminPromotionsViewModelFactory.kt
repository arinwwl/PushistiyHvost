package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.AddPromotionUseCase
import com.example.pushistiyhvost.domain.usecase.GetPromotionByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetPromotionsUseCase
import com.example.pushistiyhvost.domain.usecase.UpdatePromotionUseCase

class AdminPromotionsViewModelFactory(
    private val getPromotionsUseCase: GetPromotionsUseCase,
    private val getPromotionByIdUseCase: GetPromotionByIdUseCase,
    private val addPromotionUseCase: AddPromotionUseCase,
    private val updatePromotionUseCase: UpdatePromotionUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminPromotionsViewModel::class.java)) {
            return AdminPromotionsViewModel(
                getPromotionsUseCase = getPromotionsUseCase,
                getPromotionByIdUseCase = getPromotionByIdUseCase,
                addPromotionUseCase = addPromotionUseCase,
                updatePromotionUseCase = updatePromotionUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}