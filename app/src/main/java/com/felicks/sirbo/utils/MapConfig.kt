package com.felicks.sirbo.utils

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings


object MapConfig {

    val LP_LOCATION = LatLng(-16.489689, -68.15693)
    var mapProperties: MapProperties = MapProperties(
        mapType = MapType.NORMAL,
        isMyLocationEnabled = false,
        minZoomPreference = 5f,
        maxZoomPreference = 18f
    )
    var mapUiConfig: MapUiSettings = MapUiSettings(
        myLocationButtonEnabled = false,
        zoomControlsEnabled = true,
        mapToolbarEnabled = false
    )
    val initialState: CameraPositionState


    init {
        val lpLocation = LP_LOCATION
        initialState = CameraPositionState(
            position = CameraPosition.fromLatLngZoom(lpLocation, 12f)
        )
    }
}