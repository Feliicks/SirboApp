package com.felicks.sirbo.data.models.otpModels

import androidx.annotation.Keep

@Keep
data class RouteStopItem(
    val id: String,
    val lat: Double,
    val lon: Double,
    val name: String
)