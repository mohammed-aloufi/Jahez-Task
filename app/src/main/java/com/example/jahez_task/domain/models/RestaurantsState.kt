package com.example.jahez_task.domain.models

data class RestaurantsState(
    val isLoading: Boolean = false,
    val restaurants: List<Restaurant> = emptyList(),
    val message: String = ""
)
