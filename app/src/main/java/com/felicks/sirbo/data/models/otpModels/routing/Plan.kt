package com.felicks.sirbo.data.models.otpModels.routing

import com.felicks.sirbo.data.models.error.ErrorDetails

data class Plan(
    val date: Long,
    val from: From,
    val error: ErrorDetails? = null,
    val itineraries: List<Itinerary>? = null,
    val to: ToX
)