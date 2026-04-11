package com.example.pushistiyhvost.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pushistiyhvost.domain.usecase.GetProfileImageUseCase
import com.example.pushistiyhvost.domain.usecase.SaveProfileImageUseCase

class ProfileViewModelFactory(
    private val getProfileImageUseCase: GetProfileImageUseCase,
    private val saveProfileImageUseCase: SaveProfileImageUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                getProfileImageUseCase = getProfileImageUseCase,
                saveProfileImageUseCase = saveProfileImageUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}