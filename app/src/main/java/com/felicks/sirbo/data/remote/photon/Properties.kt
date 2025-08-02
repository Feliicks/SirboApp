package com.felicks.sirbo.data.remote.photon

data class Properties(
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
    val street: String,
    val type: String
)

fun Properties.toCompactLabel(withOsmId: Boolean = false): String {
    val location = city ?: state ?: country ?: "Ubicación sin nombre"
    val label = when {
        !name.isNullOrBlank() -> "$name, $location"
        !street.isNullOrBlank() -> "$street, $location"
        else -> location
    }

    return if (withOsmId) "$label (id: $osm_id)" else label
}

