package com.felicks.sirbo.data.models

import com.google.android.gms.maps.model.LatLng

data class LocationInfo(
    var coordinates: LatLng = LatLng(0.0, 0.0),
    var address: String = ""
)
