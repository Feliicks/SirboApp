package com.felicksdev.onlymap.di

import com.felicksdev.onlymap.data.api.OtpService
import com.felicksdev.onlymap.data.api.PhotonService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val OTP_BASE_URL = "http://192.168.1.224:8080"
    private const val KOMOOT_BASE_URL = "http://192.168.1.224:2322"


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("otp")
    fun provideOtpRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(OTP_BASE_URL) // Cambia a tu URL base
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("photon")
    fun providePhotonRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(KOMOOT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOtpService(
        @Named("otp")
        retrofit: Retrofit
    ): OtpService {
        return retrofit.create(OtpService::class.java)
    }

    @Provides
    @Singleton
    fun providePhotonService(
        @Named("photon")
        retrofit: Retrofit
    ): PhotonService {
        return retrofit.create(PhotonService::class.java)
    }
}