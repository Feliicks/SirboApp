package com.felicks.sirbo.data.models.photonModels

import com.felicks.sirbo.domain.LocationProperties

data class PropertiesDto(
    val city: String,
    val country: String,
    val countrycode: String,
    val county: String,
    val district: String,
    val extent: List<Double>,
    val locality: String,
    val name: String,
    val osm_id: Long,
    val osm_key: String,
    val osm_type: String,
    val osm_value: String,
    val state: String,
    val type: String
)

fun PropertiesDto.toDomain(): LocationProperties {
    return LocationProperties(
        country = country,
        city = city,
        locality = locality,
        county = county,
        type = type,
        district = district,
        name = name,
    )
}