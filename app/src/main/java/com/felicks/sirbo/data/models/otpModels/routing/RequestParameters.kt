package com.felicks.sirbo.data.models.otpModels.routing

import androidx.annotation.Keep

@Keep
data class RequestParameters(
    val fromPlace: String,
    val maxWalkDistance: String,
    val mode: String,
    val toPlace: String
)