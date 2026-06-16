package com.example.pushistiyhvost.presentation.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pushistiyhvost.domain.usecase.AddPetUseCase
import com.example.pushistiyhvost.domain.usecase.GetPetsUseCase
import com.example.pushistiyhvost.presentation.pets.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PetsViewModel(
    private val getPetsUseCase: GetPetsUseCase,
    private val addPetUseCase: AddPetUseCase
) : ViewModel() {

    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets

    fun loadPets(userId: String) {
        viewModelScope.launch {
            _pets.value = getPetsUseCase(userId)
        }
    }

    fun addPet(
        userId: String,
        type: String,
        breed: String,
        name: String,
        birthDate: String,
        weight: String,
        features: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch {
            addPetUseCase(
                userId = userId,
                type = type,
                breed = breed,
                name = name,
                birthDate = birthDate,
                weight = weight,
                features = features
            )
            _pets.value = getPetsUseCase(userId)
            onDone()
        }
    }
}