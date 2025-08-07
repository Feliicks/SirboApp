package com.felicks.sirbo.data.remote

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

private const val PREFIX = "sirbo/api/";
interface OtpService {

    @GET("")
    suspend fun ping(): Response<ResponseBody>

    @GET
    suspend fun getLinea(@Url url: String): Response<RutaVehicular>

//    @GET("otp/routers/default/index/routes/") //PASADO
    @GET("${PREFIX}plan/rutas")
    suspend fun indexRoutes(): Response<List<com.felicks.sirbo.data.models.otpModels.routes.RutasItem>>

    @GET
    suspend fun getRuta(@Url url: String): Response<RutasItem>
    //suspend fun getRuta(@Url url: String): Response<Ruta>

    @GET("${PREFIX}otp/routers/default/index/routes/{id}/stops")
    suspend fun getRouteStops(@Path("id") idRoute: String): Response<List<RouteStopItem>>
    // detalle de paradas de la ruta
//    @GET("otp/routers/default/index/routes/{routeId}/patterns")
    @GET("${PREFIX}plan/rutas/{routeId}/patterns")
    suspend fun getPatternByRouteId(@Path("routeId") routeId: String): Response<List<Pattern>>

//@GET("otp/routers/default/index/patterns/{patternId}")
    @GET("${PREFIX}plan/patterns/{patternId}")
suspend fun getPatternDetailsByPatternId(@Path("patternId") patternId: String): Response<PatterDetail>

//    @GET("otp/routers/default/plan")
    @GET("${PREFIX}plan")
    suspend fun fetchPlan(
        @Query("fromPlace") fromPlace: String,
        @Query("toPlace") toPlace: String,
        @Query("mode") mode: String = "TRANSIT,WALK",
        @Query("maxWalkDistance") maxWalkDistance: Int = 5000,
        @Query("maxTransfers") maxTransfers: Int = 1,
        @Query("numItineraries") numItineraries: Int = 3
    ): Response<RoutingResponse>

    /// Seu sa para la cabecera en el dealle de ruta
//    @GET("otp/routers/default/index/routes/{id}")
    @GET("${PREFIX}plan/rutas/{id}")
    suspend fun getRouteDetail(
        @Path("id") routeId: String,
    ): com.felicks.sirbo.data.models.otpModels.routes.RutasItem

    @GET("${PREFIX}otp/routers/default/plan")
//    @GET("plan")
    suspend fun getOptimalRoutes(
        @Query("fromPlace") fromPlace: String,
        @Query("toPlace") toPlace: String,
        @Query("mode") mode: String = "TRANSIT,WALK",
        @Query("maxWalkDistance") maxWalkDistance: Int = 5000
    ): Response<RoutingResponse>

    @GET("${PREFIX}otp/routers/default/index/patterns/{id}")
    suspend fun getRoutePatterns(@Path("id") routeId: String): Response<RutaVehicular>

    // GEOEMTRIA DE UNA RUTA ESPECIFICA
//    @GET("otp/routers/default/index/patterns/{patternId}/geometry")
    @GET("${PREFIX}plan/patterns/{patternId}/geometry")
    suspend fun getGeomByPattern(@Path("patternId") patternId: String): Response<PatternGeometry>

}