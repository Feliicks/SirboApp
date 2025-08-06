package com.felicks.sirbo.services

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

object RemoteConfigProvider {
    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    val otpBaseUrl: String
        get() = remoteConfig.getString("otp_base_url")
            .ifBlank { "http://10.0.2.2/sirbo/api/" }

    val photonBaseUrl: String
        get() = remoteConfig.getString("photon_base_url")
            .ifBlank { "http://10.0.2.2/geocoder/api/" }
}
