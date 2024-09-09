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

    // Función para obtener las coordenadas de fromPlace en formato "lat,lon"
    fun getFromCoordinates(): String {
        return fromPlace?.let { "${it.latitude},${it.longitude}" } ?: "Undefined"
    }

    // Función para obtener las coordenadas de toPlace en formato "lat,lon"
    fun getToCoordinates(): String {
        return toPlace?.let { "${it.latitude},${it.longitude}" } ?: "Undefined"
    }

}