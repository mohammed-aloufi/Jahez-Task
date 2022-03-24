package com.example.jahez_task.domain.models

data class AuthState(
    val isLoading: Boolean = false,
    var isSuccessful: Boolean = false,
    var message: String = ""
)