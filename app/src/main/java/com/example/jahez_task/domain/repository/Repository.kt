package com.example.jahez_task.domain.repository

import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun login(email: String, password: String): AuthState

    suspend fun register(name: String, email: String, password: String): AuthState
}