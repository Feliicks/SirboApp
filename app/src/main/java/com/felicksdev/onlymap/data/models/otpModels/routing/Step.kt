package com.felicksdev.onlymap.data.models.otpModels.routing

data class Step(
    val absoluteDirection: String,
    val area: Boolean,
    val bogusName: Boolean,
    val distance: Double,
    val elevation: List<Any>,
    val lat: Double,
    val lon: Double,
    val relativeDirection: String,
    val stayOn: Boolean,
    val streetName: String
)