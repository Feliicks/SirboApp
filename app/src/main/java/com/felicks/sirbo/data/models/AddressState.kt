package com.felicks.sirbo.data.models

import com.google.android.gms.maps.model.LatLng

data class AddressState(
    var address: String = "",
//    var latitude: Double? = null,
//    var longitude: Double? = null
    var coordinates : LatLng = LatLng(0.0, 0.0)
)