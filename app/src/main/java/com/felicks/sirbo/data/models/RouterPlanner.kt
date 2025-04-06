package com.felicks.sirbo.data.models

import com.felicks.sirbo.LocationDetail

data class RoutePlanner(
    val fromPlace: LocationDetail? = null,
    val toPlace: LocationDetail? = null,
) {
    val isPlacesDefined get() = fromPlace != null && toPlace != null

}