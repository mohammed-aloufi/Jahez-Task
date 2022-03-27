package com.example.jahez_task.data.repository

import com.example.jahez_task.data.authentication.AuthProvider
import com.example.jahez_task.data.retrofitservice.JahezApi
import com.example.jahez_task.domain.models.AuthState
import com.example.jahez_task.domain.models.Restaurant
import com.example.jahez_task.domain.models.RestaurantsResponse
import com.example.jahez_task.domain.repository.Repository
import retrofit2.Response
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val authProvider: AuthProvider,
    private val jahezApi: JahezApi
): Repository {

    override suspend fun login(email: String, password: String): AuthState {
        return authProvider.login(email, password)
    }

    override suspend fun register(name: String, email: String, password: String): AuthState {
        return authProvider.register(name, email, password)
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return authProvider.isLoggedIn
    }

    override suspend fun getAllRestaurants(): Response<List<Restaurant>> {
        return jahezApi.getAllRestaurants()
    }


}