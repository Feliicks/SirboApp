package com.felicksdev.onlymap.data.models.otpModels.routing

data class ToLeg(
    val arrival: Long,
    val lat: Double,
    val lon: Double,
    val name: String,
    val vertexType: String,
    val departure: Long,
    val stopId: String,
    val stopIndex: Int,
    val stopSequence: Int,
    val boardAlightType: String,
)