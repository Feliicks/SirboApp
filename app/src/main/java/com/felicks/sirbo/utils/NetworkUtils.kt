package com.felicks.sirbo.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.felicks.sirbo.data.remote.OtpService

object NetworkUtils {
    private val TAG = "NetworkUtils"
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

//    suspend fun isPhotonServerAvailable(apiService: PhotonService): Boolean {
//        return try {
//            val response = apiService.ping() // endpoint simple, tipo GET /ping o /health
//            response.isSuccessful
//        } catch (e: Exception) {
//            false
//        }
//    }

    suspend fun isOtpServerAvailable(apiService: OtpService): Boolean {
        return try {
            val response = apiService.ping() // endpoint simple, tipo GET /ping o /health
            response.isSuccessful
        } catch (e: Exception) {
            Log.e(TAG, "erro trap: $e")
            false
        }
    }

}