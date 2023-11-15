package com.felicksdev.onlymap.models

data class GeometriaRuta(
    val coordinates: List<List<List<Double>>>,
    val type: String,
    val geoJsonString: String,
)