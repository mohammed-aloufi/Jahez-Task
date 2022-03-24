package com.example.jahez_task.data.repository

import com.example.jahez_task.data.authentication.AuthProvider
import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authProvider: AuthProvider
): Repository {

    override suspend fun login(email: String, password: String): AuthState {
        return authProvider.login(email, password)
    }

    override suspend fun register(name: String, email: String, password: String): AuthState {
        return authProvider.register(name, email, password)
    }

}