package com.example.pushistiyhvost.domain.repository

import com.example.pushistiyhvost.presentation.pets.model.Pet

interface PetRepository {
    suspend fun getPets(userId: String): List<Pet>

    suspend fun addPet(
        userId: String,
        type: String,
        breed: String,
        name: String,
        birthDate: String,
        weight: String,
        features: String
    )
}