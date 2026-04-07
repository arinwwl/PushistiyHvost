package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.PetRepository

class AddPetUseCase(
    private val repository: PetRepository
) {
    suspend operator fun invoke(
        userId: String,
        type: String,
        breed: String,
        name: String,
        birthDate: String,
        weight: String,
        features: String
    ) {
        repository.addPet(
            userId = userId,
            type = type,
            breed = breed,
            name = name,
            birthDate = birthDate,
            weight = weight,
            features = features
        )
    }
}