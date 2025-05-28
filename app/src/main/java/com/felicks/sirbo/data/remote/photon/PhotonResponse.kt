package com.felicks.sirbo.data.remote.photon

import com.google.gson.annotations.SerializedName

data class PhotonResponse(
    @SerializedName("features")
    val photonFeatures: List<PhotonFeature>,
    val type: String
)