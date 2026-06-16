package com.example.pushistiyhvost.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.AddPromotionUseCase
import com.example.pushistiyhvost.domain.usecase.GetPromotionByIdUseCase
import com.example.pushistiyhvost.domain.usecase.GetPromotionsUseCase
import com.example.pushistiyhvost.domain.usecase.UpdatePromotionUseCase
import com.example.pushistiyhvost.presentation.admin.model.Promotion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AdminPromotionsUiState {
    data object Idle : AdminPromotionsUiState()
    data object Loading : AdminPromotionsUiState()
    data class Success(val message: String) : AdminPromotionsUiState()
    data class Error(val message: String) : AdminPromotionsUiState()
}

class AdminPromotionsViewModel(
    private val getPromotionsUseCase: GetPromotionsUseCase,
    private val getPromotionByIdUseCase: GetPromotionByIdUseCase,
    private val addPromotionUseCase: AddPromotionUseCase,
    private val updatePromotionUseCase: UpdatePromotionUseCase
) : ViewModel() {

    private val _promotions = MutableStateFlow<List<Promotion>>(emptyList())
    val promotions: StateFlow<List<Promotion>> = _promotions

    private val _selectedPromotion = MutableStateFlow<Promotion?>(null)
    val selectedPromotion: StateFlow<Promotion?> = _selectedPromotion

    private val _uiState = MutableStateFlow<AdminPromotionsUiState>(AdminPromotionsUiState.Idle)
    val uiState: StateFlow<AdminPromotionsUiState> = _uiState

    init {
        loadPromotions()
    }

    fun loadPromotions() {
        viewModelScope.launch {
            _uiState.value = AdminPromotionsUiState.Loading
            try {
                _promotions.value = getPromotionsUseCase()
                _uiState.value = AdminPromotionsUiState.Idle
            } catch (e: Exception) {
                _uiState.value = AdminPromotionsUiState.Error(
                    e.message ?: "Ошибка загрузки акций"
                )
            }
        }
    }

    fun loadPromotionById(promotionId: String) {
        viewModelScope.launch {
            _uiState.value = AdminPromotionsUiState.Loading
            try {
                _selectedPromotion.value = getPromotionByIdUseCase(promotionId)
                _uiState.value = AdminPromotionsUiState.Idle
            } catch (e: Exception) {
                _uiState.value = AdminPromotionsUiState.Error(
                    e.message ?: "Ошибка загрузки акции"
                )
            }
        }
    }

    fun clearSelectedPromotion() {
        _selectedPromotion.value = null
    }

    fun savePromotion(
        promotionId: String?,
        title: String,
        discount: String,
        banner: String,
        date: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = AdminPromotionsUiState.Loading

            if (title.isBlank() || discount.isBlank() || date.isBlank()) {
                _uiState.value = AdminPromotionsUiState.Error(
                    "Заполните название, скидку и дату"
                )
                return@launch
            }

            val promotion = Promotion(
                id = promotionId ?: "",
                title = title.trim(),
                discount = discount.trim(),
                banner = banner.trim(),
                date = date.trim()
            )

            val result = if (promotionId.isNullOrBlank()) {
                addPromotionUseCase(promotion)
            } else {
                updatePromotionUseCase(promotion)
            }

            result.fold(
                onSuccess = {
                    _uiState.value = AdminPromotionsUiState.Success("Акция сохранена")
                    loadPromotions()
                    onSuccess()
                },
                onFailure = { error ->
                    _uiState.value = AdminPromotionsUiState.Error(
                        error.message ?: "Ошибка сохранения акции"
                    )
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = AdminPromotionsUiState.Idle
    }
}