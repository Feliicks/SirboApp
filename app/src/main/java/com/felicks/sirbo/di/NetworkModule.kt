package com.felicks.sirbo.di

import com.felicks.sirbo.data.remote.OtpService
import com.felicks.sirbo.data.remote.PhotonService
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

//    private const val OTP_BASE_URL = "http://10.0.2.2:8023"
//    private const val KOMOOT_BASE_URL = "http://10.0.2.2:2380"
    private const val OTP_BASE_URL = "http://192.168.2.3:8023"
    private const val KOMOOT_BASE_URL = "http://192.168.2.3:2380"

    //    private const val OTP_BASE_URL = "http://192.168.1.201:8080"
//    private const val KOMOOT_BASE_URL = "http://192.168.1.201:2322"


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
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