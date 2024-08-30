package com.felicksdev.onlymap.data.models.otpModels.routing

data class RoutingResponse(
    val debugOutput: DebugOutput,
    val elevationMetadata: ElevationMetadata,
    val plan: Plan,
    val requestParameters: RequestParameters
)