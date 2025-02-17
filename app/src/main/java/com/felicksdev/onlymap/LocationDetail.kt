package com.felicksdev.onlymap

data class LocationDetail(
    val description: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
fun LocationDetail.isEmpty(): Boolean {
    return latitude == 0.0 && longitude == 0.0
}

fun LocationDetail.isSetted(): Boolean {
    return latitude != 0.0 && longitude != 0.0
}
