package com.felicks.sirbo.ui.presentation.screens.mainScreens

import androidx.lifecycle.ViewModel
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class MapViewModel @Inject constructor() : ViewModel() {

    private val _mapProperties = MutableStateFlow(
        MapProperties(
            mapType = MapType.NORMAL,
            isMyLocationEnabled = false, // ⛔️ No activamos ubicación hasta que haya permisos
            minZoomPreference = 5f,
            maxZoomPreference = 18f
        )
    )
    val mapProperties: StateFlow<MapProperties> = _mapProperties

    private val _mapUiConfig = MutableStateFlow(
        MapUiSettings(
            myLocationButtonEnabled = false, // ⛔️ Lo activamos después de obtener permisos
            zoomControlsEnabled = true
        )
    )
    val mapUiConfig: StateFlow<MapUiSettings> = _mapUiConfig

    // ✅ Función para habilitar la ubicación si el usuario concede permisos
    fun enableLocation() {
        _mapProperties.value = _mapProperties.value.copy(isMyLocationEnabled = true)
        _mapUiConfig.value = _mapUiConfig.value.copy(myLocationButtonEnabled = true)
    }
}

