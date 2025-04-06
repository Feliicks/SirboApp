package com.felicks.sirbo.utils

import com.google.maps.android.compose.MapUiSettings

class MapUiConfig {
    val config: MapUiSettings

    init {
        config = MapUiSettings(
            myLocationButtonEnabled = true
        )
    }

}