package com.felicks.sirbo.core.di

import android.util.Log
import com.felicks.sirbo.data.remote.OtpService
import com.felicks.sirbo.data.remote.PhotonService
import com.felicks.sirbo.data.repository.AppConfigProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TAG = "NetworkModule"
    private const val BASE_URL = "http://10.0.2.2/"  // Solo la raíz

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
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        appConfigProvider: AppConfigProvider
    ): Retrofit {
        val baseUrl = appConfigProvider.getBaseUrl()
        Log.d(TAG, "Inicializando Retrofit con base URL $baseUrl")
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOtpService(retrofit: Retrofit): OtpService {
        return retrofit.create(OtpService::class.java)
    }

    @Provides
    @Singleton
    fun providePhotonService(retrofit: Retrofit): PhotonService {
        return retrofit.create(PhotonService::class.java)
    }
}
