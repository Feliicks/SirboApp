package com.felicksdev.onlymap.data.models

import com.google.android.gms.maps.model.LatLng

data class LocationInfo(
    val coordinates: LatLng,
    val address: String
)
