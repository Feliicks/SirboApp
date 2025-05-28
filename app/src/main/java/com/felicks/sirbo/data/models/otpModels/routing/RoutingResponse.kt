package com.felicks.sirbo.data.models.otpModels.routing

import androidx.annotation.Keep

@Keep
data class RoutingResponse(
    val debugOutput: DebugOutput,
    val elevationMetadata: ElevationMetadata,
    val plan: Plan,
    val requestParameters: RequestParameters,
    val error: OtpError? = null,
)
@Keep
data class OtpError(
    val id: Int?,
    val msg: String?,
    val message: String?,
    val noPath: Boolean = false
)