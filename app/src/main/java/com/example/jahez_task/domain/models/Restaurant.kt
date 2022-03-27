package com.example.jahez_task.domain.models

import com.google.gson.annotations.SerializedName

data class Restaurant(
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
)
