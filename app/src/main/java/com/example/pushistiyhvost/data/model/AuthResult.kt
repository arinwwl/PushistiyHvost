package com.example.pushistiyhvost.data.model

data class AuthResult(
    val isSuccess: Boolean,
    val isAdmin: Boolean = false,
    val errorMessage: String? = null
)