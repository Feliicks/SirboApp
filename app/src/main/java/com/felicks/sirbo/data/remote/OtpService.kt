package com.felicks.sirbo.data.remote

import com.felicks.sirbo.core.OtpEndpoints
import com.felicks.sirbo.data.models.RutaVehicular
import com.felicks.sirbo.data.models.otpModels.PatterDetail
import com.felicks.sirbo.data.models.otpModels.Pattern
import com.felicks.sirbo.data.models.otpModels.RouteStopItem
import com.felicks.sirbo.data.models.otpModels.routes.PatternGeometry
import com.felicks.sirbo.data.models.otpModels.routes.RutasItem
import com.felicks.sirbo.data.models.otpModels.routing.RoutingResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface OtpService {

    @GET(OtpEndpoints.PING)
    suspend fun ping(): Response<ResponseBody>

    @GET
    suspend fun getLinea(@Url url: String): Response<RutaVehicular>

    @GET(OtpEndpoints.INDEX_ROUTES)
    suspend fun indexRoutes(): Response<List<RutasItem>>

    @GET
    suspend fun getRuta(@Url url: String): Response<RutasItem>

    @GET(OtpEndpoints.GET_ROUTE_STOPS)
    suspend fun getRouteStops(@Path("id") idRoute: String): Response<List<RouteStopItem>>

    @GET(OtpEndpoints.GET_PATTERN_BY_ROUTE_ID)
    suspend fun getPatternByRouteId(@Path("routeId") routeId: String): Response<List<Pattern>>

    @GET(OtpEndpoints.GET_PATTERN_DETAILS_BY_ID)
    suspend fun getPatternDetailsByPatternId(@Path("patternId") patternId: String): Response<PatterDetail>

    @GET(OtpEndpoints.FETCH_PLAN)
    suspend fun fetchPlan(
        @Query("fromPlace") fromPlace: String,
        @Query("toPlace") toPlace: String,
        @Query("mode") mode: String = "TRANSIT,WALK",
        @Query("maxWalkDistance") maxWalkDistance: Int = 5000,
        @Query("maxTransfers") maxTransfers: Int = 1,
        @Query("numItineraries") numItineraries: Int = 3
    ): Response<RoutingResponse>

    @GET(OtpEndpoints.GET_ROUTE_DETAIL)
    suspend fun getRouteDetail(@Path("id") routeId: String): RutasItem

    @GET(OtpEndpoints.GET_OPTIMAL_ROUTES)
    suspend fun getOptimalRoutes(
        @Query("fromPlace") fromPlace: String,
        @Query("toPlace") toPlace: String,
        @Query("mode") mode: String = "TRANSIT,WALK",
        @Query("maxWalkDistance") maxWalkDistance: Int = 5000
    ): Response<RoutingResponse>

    @GET(OtpEndpoints.GET_ROUTE_PATTERNS)
    suspend fun getRoutePatterns(@Path("id") routeId: String): Response<RutaVehicular>

    @GET(OtpEndpoints.GET_GEOM_BY_PATTERN)
    suspend fun getGeomByPattern(@Path("patternId") patternId: String): Response<PatternGeometry>

}
