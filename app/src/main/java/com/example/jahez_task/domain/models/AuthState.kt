package com.example.jahez_task.domain.models

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val message: String = ""
)