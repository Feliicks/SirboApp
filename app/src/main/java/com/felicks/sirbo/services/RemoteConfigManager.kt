package com.felicks.sirbo.services

import android.util.Log
import com.felicks.sirbo.data.repository.AppConfigRepository
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RemoteConfigManager @Inject constructor(
    private val repository: AppConfigRepository
) {
    private val remoteConfig = Firebase.remoteConfig

    suspend fun fetchAndStoreBaseUrl() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // Para pruebas
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mapOf("base_url" to "http://10.0.2.2")) // por defecto

        remoteConfig.fetchAndActivate().await()

        var baseUrl = remoteConfig.getString("base_url")
        Log.d("RemoteConfig", "Base URL: $baseUrl")
        // Asegurar que termine en /
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/"
            Log.d("RemoteConfig", "Base URL corregida: $baseUrl")
        }

        if (baseUrl.isNotEmpty()) {
            repository.saveBaseUrl(baseUrl)
        }
    }
}


//class RemoteConfigManager @Inject constructor(
//    private val repository: AppConfigRepository
//) {
//    private val remoteConfig = Firebase.remoteConfig
//
//    suspend fun fetchAndStoreBaseUrl() {
//        val configSettings = remoteConfigSettings {
//            minimumFetchIntervalInSeconds = 0 // Para pruebas: permite fetch sin esperar
//        }
//        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.setDefaultsAsync(mapOf("base_url" to "http://10.0.2.2")) // Valor por defecto
//
//        try {
//            val activated = remoteConfig.fetchAndActivate().await()
//            Log.d("RemoteConfig", "Remote config activated: $activated")
//
//            val baseUrl = remoteConfig.getString("base_url")
//            Log.d("RemoteConfig", "Fetched base_url from remote config: $baseUrl")
//
//            if (baseUrl.isNotBlank()) {
//                repository.saveBaseUrl(baseUrl)
//                Log.d("RemoteConfig", "Base URL saved to repository: $baseUrl")
//            } else {
//                Log.w("RemoteConfig", "Base URL is blank, not saving.")
//            }
//        } catch (e: Exception) {
//            Log.e("RemoteConfig", "Failed to fetch remote config", e)
//        }
//    }
//}