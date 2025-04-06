package com.felicks.sirbo.data.models.otpModels.routing

data class FromLeg(
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var name: String = "",
    var orig: String = "",
    var vertexType: String = ""
)