package com.felicks.sirbo.data.models.otpModels.routing

data class To(
    val arrival: Long,
    val lat: Double,
    val lon: Double,
    val name: String,
    val orig: String,
    val vertexType: String
)