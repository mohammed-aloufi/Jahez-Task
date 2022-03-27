package com.example.jahez_task.domain.repository

import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.models.Restaurant
import com.example.jahez_task.domain.models.RestaurantsResponse
import retrofit2.Response

interface Repository {

    suspend fun login(email: String, password: String): AuthState

    suspend fun register(name: String, email: String, password: String): AuthState

    suspend fun isUserLoggedIn(): Boolean

    suspend fun getAllRestaurants(): Response<List<Restaurant>>
}