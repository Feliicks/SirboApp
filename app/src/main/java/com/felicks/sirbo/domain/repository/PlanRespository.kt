package com.felicks.sirbo.domain.repository

import com.felicks.sirbo.data.models.otpModels.PatterDetail
import com.felicks.sirbo.data.models.otpModels.Pattern
import com.felicks.sirbo.data.models.otpModels.routes.PatternGeometry
import com.felicks.sirbo.data.models.otpModels.routes.RutasItem
import com.felicks.sirbo.data.models.otpModels.routing.RoutingResponse
import com.felicks.sirbo.domain.models.OtpConfig
import retrofit2.Response

interface PlanRespository {
    suspend fun fetchPlan(from: String, to: String, config : OtpConfig): Response<RoutingResponse>
    suspend fun fetchRoutes(): Response<List<RutasItem>>
    suspend fun getRouteDetail(id: String): RutasItem
    suspend fun getPatternByRouteId(routeId: String): Response<List<Pattern>>
    suspend fun getPatternDetailsByPatternId(patternId: String): Response<PatterDetail>
    suspend fun getGeomByPattern(patternId: String): Response<PatternGeometry>
    suspend fun getOptimalRoutes(from: String, to: String): Response<RoutingResponse>
}