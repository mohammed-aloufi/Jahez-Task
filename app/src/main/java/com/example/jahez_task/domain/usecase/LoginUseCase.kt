package com.example.jahez_task.domain.usecase

import com.example.jahez_task.domain.models.AuthState
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
    ): Flow<Resource<AuthState>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.login(email, password)
            if (response.isSuccessful){
                emit(Resource.Success(response))
            }else {
                emit(Resource.Error(message = response.message))
            }
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }
}