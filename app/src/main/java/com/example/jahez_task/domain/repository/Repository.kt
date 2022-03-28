package com.example.jahez_task.domain.repository

import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.models.Resource
import com.example.jahez_task.domain.models.Restaurant
import com.example.jahez_task.domain.models.RestaurantDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repository {

    suspend fun login(email: String, password: String): Flow<Resource<Boolean>>

    suspend fun register(name: String, email: String, password: String): Flow<Resource<Boolean>>

    suspend fun isUserLoggedIn(): Flow<Boolean>

    suspend fun getAllRestaurants(): Flow<Resource<List<Restaurant>>>
}