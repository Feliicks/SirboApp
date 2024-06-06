package com.felicksdev.onlymap.services.network

import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.models.RutaVehicular
import com.felicksdev.onlymap.data.models.otpModels.PatterDetail
import com.felicksdev.onlymap.data.models.otpModels.Pattern
import com.felicksdev.onlymap.data.models.otpModels.RouteStopItem
import com.felicksdev.onlymap.data.models.otpModels.RoutesModelItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface MyApiService {
    @GET
    suspend fun getLinea(@Url url: String): Response<RutaVehicular>

    @GET("otp/routers/default/index/routes/")
    suspend fun getAllRutas(): Response<List<RoutesModelItem>>

    @GET
    suspend fun getRuta(@Url url: String): Response<Ruta>
    //suspend fun getRuta(@Url url: String): Response<Ruta>

    @GET("otp/routers/default/index/routes/{id}/stops")
    suspend fun getRouteStops(@Path("id") idRoute: String): Response<List<RouteStopItem>>

    @GET("otp/routers/default/index/routes/{routeId}/patterns")
    suspend fun getPatternByRouteId(@Path("routeId") routeId: String): Response<List<Pattern>>

    @GET("otp/routers/default/index/patterns/{patternId}")
    suspend fun getPatternDetailsByPatternId(@Path("patternId") patternId: String): Response<PatterDetail>

    @GET("otp/routers/default/index/patterns/{id}")
    suspend fun getPattern(@Path("id") idPattern: String): Response<RutaVehicular>


    @GET("otp/routers/default/index/patterns/{id}")
    suspend fun getRoutePatterns(@Path("id") routeId: String): Response<RutaVehicular>



}