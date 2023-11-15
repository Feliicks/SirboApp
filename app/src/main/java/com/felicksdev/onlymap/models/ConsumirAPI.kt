package com.felicksdev.onlymap.models

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ConsumirAPI {
    //@GET("ruta/nombre/893")
    //@GET("ruta/522")
    @GET
    fun getRutas(@Url url: String): Response<RutaResponse>
}