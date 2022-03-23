package com.example.jahez_task.domain.usecase

import com.example.jahez_task.domain.repository.Repository
import com.example.jahez_task.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.login(email, password)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }
}