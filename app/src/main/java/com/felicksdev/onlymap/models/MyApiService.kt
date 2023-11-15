package com.felicksdev.onlymap.models

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface MyApiService {
    @GET
    suspend fun getLinea(@Url url: String): Response<RutaVehicular>

    @GET
    suspend fun getRuta(@Url url: String): Response<Ruta>
    //suspend fun getRuta(@Url url: String): Response<Ruta>

}