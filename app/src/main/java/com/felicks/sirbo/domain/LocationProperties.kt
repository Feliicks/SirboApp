package com.felicks.sirbo.domain

data class LocationProperties(
    var city: String = "",
    var country: String = "",
    var countrycode: String = "",
    var county: String = "",
    var district: String? =  null,
    var locality: String? = null,
    var name: String = "",
    var osm_id: Int = 0,
    var osm_key: String = "",
    var osm_type: String = "",
    var osm_value: String = "",
    var state: String = "",
    var type: String = "",
    var street: String? = null,
)