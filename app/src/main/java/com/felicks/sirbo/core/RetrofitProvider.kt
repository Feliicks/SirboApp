package com.felicks.sirbo.core

import com.felicks.sirbo.data.repository.AppConfigRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    private var retrofit: Retrofit? = null
    private var currentBaseUrl: String? = null

    suspend fun <T> createService(
        appConfigRepository: AppConfigRepository,
        serviceClass: Class<T>
    ): T {
        val baseUrl = appConfigRepository.getBaseUrl()
            ?: throw IllegalStateException("No baseUrl found in DB")

        if (retrofit == null || currentBaseUrl != baseUrl) {
            currentBaseUrl = baseUrl
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build()
                )
                .build()
        }

        return retrofit!!.create(serviceClass)
    }
}
