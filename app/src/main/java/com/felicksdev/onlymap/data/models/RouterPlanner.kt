package com.felicksdev.onlymap.data.models

import com.felicksdev.onlymap.LocationDetail

data class RoutePlanner(
    val fromPlace: LocationDetail? = null,
    val toPlace: LocationDetail? = null,
) {
    val isPlacesDefined get() = fromPlace != null && toPlace != null

}