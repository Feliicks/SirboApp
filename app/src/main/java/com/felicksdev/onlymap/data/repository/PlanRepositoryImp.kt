package com.felicksdev.onlymap.data.repository

import android.util.Log
import com.felicksdev.onlymap.data.models.otpModels.PatterDetail
import com.felicksdev.onlymap.data.models.otpModels.Pattern
import com.felicksdev.onlymap.data.models.otpModels.routes.PatternGeometry
import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.data.models.otpModels.routing.RoutingResponse
import com.felicksdev.onlymap.data.remote.OtpService
import com.felicksdev.onlymap.domain.repository.PlanRespository
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class PlanRepositoryImp @Inject constructor(
    private val apiService: OtpService
) : PlanRespository {
    override suspend fun fetchPlan(from: String, to: String): Response<RoutingResponse> {
        val response = apiService.fetchPlan(from, to)
        Log.d("PlanRepositoryImp", "fetchPlan: api response ${Gson().toJson(response.body())}")
        return response
    }

    override suspend fun fetchRoutes(): Response<List<RoutesItem>> {
        return apiService.indexRoutes()
    }

    override suspend fun getRouteDetail(id: String): RoutesItem {
        return apiService.getRouteDetail(id)
    }

    override suspend fun getPatternByRouteId(routeId: String): Response<List<Pattern>> {
        return apiService.getPatternByRouteId(routeId)
    }

    override suspend fun getPatternDetailsByPatternId(patternId: String): Response<PatterDetail> {
//        TODO("Not yet implemented")
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