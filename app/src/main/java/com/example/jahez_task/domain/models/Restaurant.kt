package com.example.jahez_task.domain.models

import com.google.gson.annotations.SerializedName

data class Restaurant(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val hours: String = "",
    val imageUrl: String = "",
    val rating: String = "",
    val distance: String = "",
    val hasOffer: Boolean = false,
    val offer: String = ""
)
