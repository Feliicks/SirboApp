package com.felicksdev.onlymap.data.network

import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.models.RutaVehicular
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface MyApiService {
    @GET
    suspend fun getLinea(@Url url: String): Response<RutaVehicular>

    @GET("ruta")
    suspend fun getAllRutas(): Response<List<Ruta>>

    @GET
    suspend fun getRuta(@Url url: String): Response<Ruta>
    //suspend fun getRuta(@Url url: String): Response<Ruta>

}