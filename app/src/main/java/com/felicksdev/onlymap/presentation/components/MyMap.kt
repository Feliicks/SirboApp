package com.felicksdev.onlymap.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.felicksdev.onlymap.data.models.otpModels.routing.Itinerary
import com.felicksdev.onlymap.utils.MapConfig
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline

@Composable
fun MyMap(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    mapConfiguration: MapProperties = MapConfig.mapProperties,
    mapUiConfiguration: MapUiSettings = MapConfig.mapUiConfig,
    padding: PaddingValues = PaddingValues(0.dp),
    markers: @Composable () -> Unit = {},
    itinerary: Itinerary? = null,
    isplacesDefined: Boolean = false
) {
    LaunchedEffect (itinerary) {
        itinerary?.let {
            try {
                // Crear los límites (LatLngBounds) a partir de los puntos de todas las polilíneas
                val boundsBuilder = LatLngBounds.Builder()
                it.legs.forEach { leg ->
                    val polylinePoints = PolyUtil.decode(leg.legGeometry.points)
                    polylinePoints.forEach { point ->
                        boundsBuilder.include(point)
                    }
                }
                val bounds = boundsBuilder.build()
                // Ejecutar la animación de la cámara en una corrutina dentro de LaunchedEffect
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngBounds(bounds, 80), // Ajusta el padding si es necesario
                )
            } catch (e: IllegalStateException) {
                e.printStackTrace() // Maneja el error para evitar crasheos
            }
        }
    }
    GoogleMap(
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiConfiguration,
        properties = mapConfiguration,
    ) {
            //Verificart si es nulo
            itinerary?.let {
                // Determinar el color de la polilínea según el modo de transporte
                it.legs.forEach { leg ->
                    val polylinePoints = PolyUtil.decode(leg.legGeometry.points)
                    val polylineColor = when (leg.mode) {
                        "BUS" -> Color.Red
                        "WALK" -> Color.Gray
                        else -> Color.Blue
                    }
                    // Dibujar la polilínea con los puntos de la ruta
                    Polyline(
                        points = polylinePoints,
                        color = polylineColor,
                        width = 5f,
                    )
                }
            }



        markers()
    }
}

@Preview
@Composable
private fun MuMapPreview() {
    MyMap(cameraPositionState = MapConfig.initialState)
}