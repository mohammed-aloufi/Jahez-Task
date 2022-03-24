package com.example.jahez_task.domain.repository

import com.example.jahez_task.domain.models.AuthState

interface Repository {

    suspend fun login(email: String, password: String): AuthState

    suspend fun register(name: String, email: String, password: String): AuthState
}