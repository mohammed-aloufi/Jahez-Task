package com.example.jahez_task.domain.usecase

import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.repository.Repository
import com.example.jahez_task.domain.models.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<Boolean>> = repository.register(name, email, password)
}