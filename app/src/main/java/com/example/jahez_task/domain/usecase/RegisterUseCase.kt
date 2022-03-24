package com.example.jahez_task.domain.usecase

import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.repository.Repository
import com.example.jahez_task.utils.InputValidator.isValidEmail
import com.example.jahez_task.utils.InputValidator.isValidPassword
import com.example.jahez_task.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<AuthState>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.register(name, email, password)
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