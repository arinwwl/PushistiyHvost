package com.example.pushistiyhvost.domain.usecase

import com.example.pushistiyhvost.domain.repository.PetRepository

class GetPetsUseCase(
    private val repository: PetRepository
) {
    suspend operator fun invoke(userId: String) = repository.getPets(userId)
}