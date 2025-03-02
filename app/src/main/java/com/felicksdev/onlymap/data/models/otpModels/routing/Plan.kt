package com.felicksdev.onlymap.data.models.otpModels.routing

import com.felicksdev.onlymap.data.models.error.ErrorDetails

data class Plan(
    val date: Long,
    val from: From,
    val error: ErrorDetails? = null,
    val itineraries: List<Itinerary>? = null,
    val to: ToX
)