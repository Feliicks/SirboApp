package com.felicks.sirbo.data.remote

import com.felicks.sirbo.data.models.photonModels.ReverseResponse
import com.felicks.sirbo.data.remote.photon.PhotonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val PREFIX = "geocoder/api/"
interface PhotonService {
    @GET("${PREFIX}reverse")
    suspend fun getAdressByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Response<ReverseResponse>

    @GET("${PREFIX}api")
    suspend fun searchPlaces(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5
    ): Response<PhotonResponse>

}