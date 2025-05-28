package com.felicks.sirbo.data.models.otpModels.routing

import androidx.annotation.Keep
import com.felicks.sirbo.data.models.error.ErrorDetails
@Keep
data class Plan(
    val date: Long,
    val from: From,
    val error: ErrorDetails? = null,
    val itineraries: List<Itinerary>? = null,
    val to: ToX
)