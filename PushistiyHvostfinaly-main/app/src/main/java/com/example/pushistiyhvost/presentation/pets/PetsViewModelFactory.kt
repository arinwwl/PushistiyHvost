package com.example.pushistiyhvost.presentation.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.AddPetUseCase
import com.example.pushistiyhvost.domain.usecase.GetPetsUseCase

class PetsViewModelFactory(
    private val getPetsUseCase: GetPetsUseCase,
    private val addPetUseCase: AddPetUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PetsViewModel::class.java)) {
            return PetsViewModel(getPetsUseCase, addPetUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}