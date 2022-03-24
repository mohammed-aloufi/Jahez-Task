package com.example.jahez_task.data.authentication

import com.example.jahez_task.domain.models.AuthState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface AuthProvider {

    suspend fun login(email: String, password: String): AuthState

    suspend fun register(name: String, email: String, password: String): AuthState
}
