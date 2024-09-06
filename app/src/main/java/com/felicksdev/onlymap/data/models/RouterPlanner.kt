package com.felicksdev.onlymap.data.models

import com.felicksdev.onlymap.TrufiLocation

data class RoutePlanner(
    val fromPlace: TrufiLocation? = null,
    val toPlace: TrufiLocation? = null,
//    val plan: Plan? = null,
//    val selectedItinerary: Itinerary? = null
) {
    val isPlacesDefined get() = fromPlace != null && toPlace != null
//    val isPlanCorrect get() = isPlacesDefined && plan != null && selectedItinerary != null
}