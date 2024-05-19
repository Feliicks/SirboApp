package com.felicksdev.onlymap.utils

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings


class MapConfig {
    var mapProperties: MapProperties
    val mapUiConfig: MapUiSettings
    val initialState: CameraPositionState


    init {
        mapProperties = MapProperties(
            mapType = MapType.NORMAL,
            isMyLocationEnabled = true
        )
        mapUiConfig = MapUiSettings(
            myLocationButtonEnabled = true
        )
        val lpLocation = LatLng(-16.489689, -68.15693)
        val Location = LatLng(1.35, 103.87)
        initialState = CameraPositionState(
            position = CameraPosition.fromLatLngZoom(lpLocation, 12f)
        )
    }
}