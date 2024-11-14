package com.felicksdev.onlymap.services.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
    fun otpRetrofit(): MyApiService {
        return Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8080/")
            .baseUrl("http://192.168.1.216:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApiService::class.java)
    }
    // Retrofit instance for Photon Komoot
    private val photonRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.photon.komoot.io") // Cambia a tu URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}