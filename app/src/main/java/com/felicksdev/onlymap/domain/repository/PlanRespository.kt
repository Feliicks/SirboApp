package com.felicksdev.onlymap.domain.repository

import com.felicksdev.onlymap.data.models.otpModels.PatterDetail
import com.felicksdev.onlymap.data.models.otpModels.Pattern
import com.felicksdev.onlymap.data.models.otpModels.routes.PatternGeometry
import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.data.models.otpModels.routing.RoutingResponse
import retrofit2.Response

interface PlanRespository {
    suspend fun fetchPlan(from: String, to: String): Response<RoutingResponse>
    suspend fun fetchRoutes(): Response<List<RoutesItem>>
    suspend fun getRouteDetail(id: String): RoutesItem
    suspend fun getPatternByRouteId(routeId: String): Response<List<Pattern>>
    suspend fun getPatternDetailsByPatternId(patternId: String): Response<PatterDetail>
    suspend fun getGeomByPattern(patternId: String): Response<PatternGeometry>
    suspend fun getOptimalRoutes(from: String, to: String): Response<RoutingResponse>
}