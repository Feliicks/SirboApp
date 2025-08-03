package com.felicks.sirbo.data.repository

import com.felicks.sirbo.data.models.otpModels.PatterDetail
import com.felicks.sirbo.data.models.otpModels.Pattern
import com.felicks.sirbo.data.models.otpModels.routes.PatternGeometry
import com.felicks.sirbo.data.models.otpModels.routes.RutasItem
import com.felicks.sirbo.data.models.otpModels.routing.RoutingResponse
import com.felicks.sirbo.data.remote.OtpService
import com.felicks.sirbo.domain.models.OtpConfig
import com.felicks.sirbo.domain.repository.PlanRespository
import retrofit2.Response
import javax.inject.Inject

class PlanRepositoryImp @Inject constructor(
    private val apiService: OtpService
) : PlanRespository {
    override suspend fun fetchPlan(
        from: String,
        to: String,
        config: OtpConfig
    ): Response<RoutingResponse> {
        return apiService.fetchPlan(
            fromPlace = from,
            toPlace = to,
            mode = config.mode,
            maxWalkDistance = config.walkDistance,
            maxTransfers = config.maxTransfers,
            numItineraries = config.numItineraries
        )
    }

    override suspend fun fetchRoutes(): Response<List<RutasItem>> {
        return apiService.indexRoutes()
    }

    override suspend fun getRouteDetail(id: String): RutasItem {
        return apiService.getRouteDetail(id)
    }

    override suspend fun getPatternByRouteId(routeId: String): Response<List<Pattern>> {
        return apiService.getPatternByRouteId(routeId)
    }

    override suspend fun getPatternDetailsByPatternId(patternId: String): Response<PatterDetail> {
        return apiService.getPatternDetailsByPatternId(patternId)
    }

    override suspend fun getGeomByPattern(patternId: String): Response<PatternGeometry> {
        return apiService.getGeomByPattern(patternId)
    }

    override suspend fun getOptimalRoutes(
        from: String,
        to: String
    ): Response<RoutingResponse> {
        return apiService.getOptimalRoutes(from, to)
    }

}