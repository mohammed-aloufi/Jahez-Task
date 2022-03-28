package com.example.jahez_task.domain.models

import com.example.jahez_task.utils.roundOffDecimal
import com.google.gson.annotations.SerializedName

data class RestaurantDto(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val hours: String = "",
    @SerializedName("image")
    val imageUrl: String = "",
    val rating: Int = 0,
    val distance: Double = 0.0,
    val hasOffer: Boolean = false,
    val offer: String = ""
){
    fun toRestaurant(): Restaurant{
        return Restaurant(
            id = id,
            name = name,
            hours = hours,
            imageUrl = imageUrl,
            rating = rating.toString(),
            distance = distance.roundOffDecimal(),
            hasOffer = hasOffer,
            offer = offer
        )
    }
}
