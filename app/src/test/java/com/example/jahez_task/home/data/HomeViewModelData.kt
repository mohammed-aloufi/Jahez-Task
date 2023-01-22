package com.example.jahez_task.home.data

import com.example.jahez_task.domain.models.Restaurant

object HomeViewModelData {

    val restaurants = listOf(
        Restaurant(
            0,
            "Kudu",
            "Enjoy fast delivery from Jahez. Order now, or schedule your order any time you want",
            "05:30 AM - 04:30 AM",
            "https://jahez-other-oniiphi8.s3.eu-central-1.amazonaws.com/1.jpg",
            "8",
            "1.8",
            false
        )
    )

    val emptyRestaurantsList = emptyList<Restaurant>()
}