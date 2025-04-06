package com.felicks.sirbo.data.models

data class GeometriaRuta(
    val coordinates: List<List<List<Double>>>,
    val type: String,
    val geoJsonString: String,
)