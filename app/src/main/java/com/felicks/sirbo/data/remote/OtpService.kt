package com.felicks.sirbo.data.remote

import com.felicks.sirbo.data.models.Ruta
import com.felicks.sirbo.data.models.RutaVehicular
import com.felicks.sirbo.data.models.otpModels.PatterDetail
import com.felicks.sirbo.data.models.otpModels.Pattern
import com.felicks.sirbo.data.models.otpModels.RouteStopItem
import com.felicks.sirbo.data.models.otpModels.routes.PatternGeometry
import com.felicks.sirbo.data.models.otpModels.routes.RoutesItem
import com.felicks.sirbo.data.models.otpModels.routing.RoutingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface OtpService {
    @GET
    suspend fun getLinea(@Url url: String): Response<RutaVehicular>

    @GET("otp/routers/default/index/routes/")
    suspend fun indexRoutes(): Response<List<RoutesItem>>

    @GET
    suspend fun getRuta(@Url url: String): Response<Ruta>
    //suspend fun getRuta(@Url url: String): Response<Ruta>

    @GET("otp/routers/default/index/routes/{id}/stops")
    suspend fun getRouteStops(@Path("id") idRoute: String): Response<List<RouteStopItem>>

    @GET("otp/routers/default/index/routes/{routeId}/patterns")
    suspend fun getPatternByRouteId(@Path("routeId") routeId: String): Response<List<Pattern>>

    @GET("otp/routers/default/index/patterns/{patternId}")
    suspend fun getPatternDetailsByPatternId(@Path("patternId") patternId: String): Response<PatterDetail>

    @GET("otp/routers/default/plan")
    suspend fun fetchPlan(
        @Query("fromPlace") fromPlace: String,
        @Query("toPlace") toPlace: String,
        @Query("mode") mode: String = "TRANSIT,WALK",
        @Query("maxWalkDistance") maxWalkDistance: Int = 5000,
        @Query("maxTransfers") maxTransfers: Int = 1,
        @Query("numItineraries") numItineraries: Int = 3
    ): Response<RoutingResponse>


    @GET("otp/routers/default/index/routes/{id}")
    suspend fun getRouteDetail(
        @Path("id") routeId: String,
    ): RoutesItem

    @GET("otp/routers/default/plan")
    suspend fun getOptimalRoutes(
        @Query("fromPlace") fromPlace: String,
        @Query("toPlace") toPlace: String,
        @Query("mode") mode: String = "TRANSIT,WALK",
        @Query("maxWalkDistance") maxWalkDistance: Int = 5000
    ): Response<RoutingResponse>

    @GET("otp/routers/default/index/patterns/{id}")
    suspend fun getRoutePatterns(@Path("id") routeId: String): Response<RutaVehicular>


    @GET("otp/routers/default/index/patterns/{patternId}/geometry")
    suspend fun getGeomByPattern(@Path("patternId") patternId: String): Response<PatternGeometry>

}