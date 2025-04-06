package com.felicks.sirbo.data.models.otpModels.routing

data class RequestParameters(
    val fromPlace: String,
    val maxWalkDistance: String,
    val mode: String,
    val toPlace: String
)