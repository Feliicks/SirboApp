package com.felicksdev.onlymap.data.api

import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.models.RutaVehicular
import com.felicksdev.onlymap.data.models.otpModels.PatterDetail
import com.felicksdev.onlymap.data.models.otpModels.Pattern
import com.felicksdev.onlymap.data.models.otpModels.RouteStopItem
import com.felicksdev.onlymap.data.models.otpModels.routes.PatternGeometry
import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.data.models.otpModels.routing.RoutingResponse
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
    suspend fun getOptimalRoutes(
        @Query("fromPlacez") fromPlace: String,
        @Query("toPlace") toPlace: String,
        @Query("mode") mode: String = "TRANSIT,WALK",
        @Query("maxWalkDistance") maxWalkDistance: Int = 5000
    ): Response<RoutingResponse>


    @GET("otp/routers/default/plan")
    suspend fun fetchItineraries(
        @Query("fromPlace") fromPlace: String,
        @Query("toPlace") toPlace: String,
        @Query("mode") mode: String = "TRANSIT,WALK",
        @Query("maxWalkDistance") maxWalkDistance: Int = 5000
    ): Response<RoutingResponse>

    @GET("otp/routers/default/index/routes/{id}")
    suspend fun getRouteDetail(
        @Path("id") routeId: String,
    ): RoutesItem


    @GET("otp/routers/default/index/patterns/{id}")
    suspend fun getRoutePatterns(@Path("id") routeId: String): Response<RutaVehicular>


    @GET("otp/routers/default/index/patterns/{patternId}/geometry")
    suspend fun getGeomByPattern(@Path("patternId") patternId: String): Response<PatternGeometry>

}