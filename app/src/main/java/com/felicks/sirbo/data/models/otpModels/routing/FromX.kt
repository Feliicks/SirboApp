package com.felicks.sirbo.data.models.otpModels.routing

data class FromX(
    val departure: Long,
    val lat: Double,
    val lon: Double,
    val name: String,
    val orig: String,
    val vertexType: String
)