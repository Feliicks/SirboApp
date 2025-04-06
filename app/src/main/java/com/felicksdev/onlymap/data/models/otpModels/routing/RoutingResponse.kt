package com.felicksdev.onlymap.data.models.otpModels.routing

data class RoutingResponse(
    val debugOutput: DebugOutput,
    val elevationMetadata: ElevationMetadata,
    val plan: Plan,
    val requestParameters: RequestParameters,
    val error: OtpError? = null,
)
data class OtpError(
    val id: Int?,
    val msg: String?,
    val message: String?,
    val noPath: Boolean = false
)