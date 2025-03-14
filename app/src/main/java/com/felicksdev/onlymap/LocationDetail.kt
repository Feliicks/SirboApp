package com.felicksdev.onlymap

import com.google.android.gms.maps.model.LatLng

data class LocationDetail(
    val description: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

fun LocationDetail.isEmpty(): Boolean {
    return latitude == 0.0 && longitude == 0.0
}

fun LocationDetail.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}

fun LocationDetail.isSetted(): Boolean {
    return latitude != 0.0 && longitude != 0.0
}
