package com.felicksdev.onlymap.data.models.otpModels.routing

data class ToLeg(
    var arrival: Long = 0,
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var name: String = "",
    var vertexType: String = "",
    var departure: Long = 0,
    var stopId: String = "",
    var stopIndex: Int = 0,
    var stopSequence: Int = 0,
    var boardAlightType: String = "",
)