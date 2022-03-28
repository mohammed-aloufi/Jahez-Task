package com.example.jahez_task.data.retrofitservice

import com.example.jahez_task.domain.models.RestaurantDto
import retrofit2.Response
import retrofit2.http.GET

interface JahezApi {

    @GET("restaurants.json")
    suspend fun getAllRestaurants(): Response<List<RestaurantDto>>
}