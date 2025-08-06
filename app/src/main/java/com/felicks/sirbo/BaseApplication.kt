package com.felicks.sirbo

import android.app.Application
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600) // cada 1 hora
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(
            mapOf(
                "otp_base_url" to "http://10.0.2.2/sirbo/api/",
                "photon_base_url" to "http://10.0.2.2/geocoder/api/"
            )
        )

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener{
                task ->
            if (task.isSuccessful) {
                Log.d("RemoteConfig", "Config updated")
            } else {
                Log.w("RemoteConfig", "Fetch failed")
            }
        }
    }
}