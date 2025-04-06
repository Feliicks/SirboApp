package com.felicks.sirbo.data.remote

import com.felicks.sirbo.data.models.photonModels.ReverseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotonService {
    @GET("/reverse")
    suspend fun getAdressByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Response<ReverseResponse>

}