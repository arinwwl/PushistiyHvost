package com.example.pushistiyhvost.data.model

import com.example.pushistiyhvost.presentation.pets.model.Pet

fun PetDto.toPet(): Pet {
    return Pet(
        id = id,
        userId = userId,
        type = type,
        breed = breed,
        name = name,
        birthDate = birthDate,
        weight = weight,
        features = features
    )
}