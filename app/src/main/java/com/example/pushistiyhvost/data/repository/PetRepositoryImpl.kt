package com.example.pushistiyhvost.data.repository

import com.example.pushistiyhvost.data.model.PetDto
import com.example.pushistiyhvost.data.model.toPet
import com.example.pushistiyhvost.domain.repository.PetRepository
import com.example.pushistiyhvost.presentation.pets.model.Pet
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PetRepositoryImpl(
    private val firestore: FirebaseFirestore
) : PetRepository {

    override suspend fun getPets(userId: String): List<Pet> {
        return try {
            val snapshot = firestore.collection("pets")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                document.toObject(PetDto::class.java)
                    ?.copy(id = document.id)
                    ?.toPet()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addPet(
        userId: String,
        type: String,
        breed: String,
        name: String,
        birthDate: String,
        weight: String,
        features: String
    ) {
        val petId = firestore.collection("pets").document().id

        val petDto = PetDto(
            id = petId,
            userId = userId,
            type = type,
            breed = breed,
            name = name,
            birthDate = birthDate,
            weight = weight,
            features = features
        )

        firestore.collection("pets")
            .document(petId)
            .set(petDto)
            .await()
    }
}