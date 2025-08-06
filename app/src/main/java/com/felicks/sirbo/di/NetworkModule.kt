package com.felicks.sirbo.di

import android.util.Log
import com.felicks.sirbo.data.remote.OtpService
import com.felicks.sirbo.data.remote.PhotonService
import com.felicks.sirbo.services.RemoteConfigProvider
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
    private const val TAG = "NetworkModule";

    //    private const val BASE_URL = "https://f-decline-conditional-glen.trycloudflare.com/";
    private const val BASE_URL = "http://10.0.2.2";
    private const val OTP_BASE_URL = "${BASE_URL}/sirbo/api/"
    private const val KOMOOT_BASE_URL = "${BASE_URL}/geocoder/api/"

//    @Provides
//    @Singleton
//    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
//        val config = FirebaseRemoteConfig.getInstance()
//        config.setConfigSettingsAsync(
//            FirebaseRemoteConfigSettings.Builder()
//                .setMinimumFetchIntervalInSeconds(3600)
//                .build()
//        )
//        config.setDefaultsAsync(
//            mapOf(
//                "otp_base_url" to "http://10.0.2.2/sirbo/api/",
//                "photon_base_url" to "http://10.0.2.2/geocoder/api/"
//            )
//        )
//        return config
//    }

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
        Log.d(TAG,"Inicianod OTP url con ${RemoteConfigProvider.otpBaseUrl}")
        return Retrofit.Builder()
            .baseUrl(RemoteConfigProvider.otpBaseUrl) // Cambia a tu URL base
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("photon")
    fun providePhotonRetrofit(okHttpClient: OkHttpClient): Retrofit {
        Log.d(TAG,"Inicianod photon url con ${RemoteConfigProvider.photonBaseUrl}")
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