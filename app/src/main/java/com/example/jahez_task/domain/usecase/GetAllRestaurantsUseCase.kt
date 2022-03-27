package com.example.jahez_task.domain.usecase

import com.example.jahez_task.domain.models.Resource
import com.example.jahez_task.domain.models.Restaurant
import com.example.jahez_task.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAllRestaurantsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(): Flow<Resource<List<Restaurant>>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.getAllRestaurants()
            if (response.isSuccessful){
                emit(Resource.Success(response.body()))
            } else {
                emit(Resource.Error(message = response.message()))
            }
        }catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }
}