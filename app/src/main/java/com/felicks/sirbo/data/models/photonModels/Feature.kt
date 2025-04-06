package com.felicks.sirbo.data.models.photonModels

data class Feature(
    val geometry: Geometry,
    val properties: PropertiesDto,
    val type: String
)