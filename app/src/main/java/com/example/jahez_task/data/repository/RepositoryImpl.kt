package com.example.jahez_task.data.repository

import com.example.jahez_task.data.authentication.AuthProvider
import com.example.jahez_task.data.retrofitservice.JahezApi
import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.models.Resource
import com.example.jahez_task.domain.models.Restaurant
import com.example.jahez_task.domain.models.RestaurantDto
import com.example.jahez_task.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authProvider: AuthProvider,
    private val jahezApi: JahezApi
) : Repository {

    override suspend fun login(email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        val response = authProvider.login(email, password)
        if (response.isSuccessful) {
            emit(Resource.Success(response.isSuccessful))
        } else {
            emit(Resource.Error(message = response.message))
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        val response = authProvider.register(name, email, password)
        if (response.isSuccessful) {
            emit(Resource.Success(response.isSuccessful))
        } else {
            emit(Resource.Error(message = response.message))
        }
    }

    override suspend fun isUserLoggedIn(): Flow<Boolean> = flow {
        emit(authProvider.isLoggedIn)
    }

    override suspend fun getAllRestaurants(): Flow<Resource<List<Restaurant>>> = flow {
        emit(Resource.Loading())
        val response = jahezApi.getAllRestaurants()
        if (response.isSuccessful) {
            val restaurants = response.body()?.map {
                it.toRestaurant()
            }
            emit(Resource.Success(restaurants))
        } else {
            emit(Resource.Error(message = response.message()))
        }
    }
}