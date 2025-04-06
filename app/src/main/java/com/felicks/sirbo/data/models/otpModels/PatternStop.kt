package com.felicks.sirbo.data.models.otpModels

data class PatternStop(
    val id: String,
    val desc: Double,
    val routeId: Double,
    val stops: List<RouteStopItem>
)
