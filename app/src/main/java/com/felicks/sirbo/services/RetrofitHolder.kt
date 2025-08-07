package com.felicks.sirbo.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyRetrofitHolder {
    private var retrofit: Retrofit? = null

    fun rebuild(baseUrl: String) {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun get(): Retrofit {
        return retrofit ?: throw IllegalStateException("Retrofit not initialized")
    }
}
