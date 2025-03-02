package com.felicksdev.onlymap.ui.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.felicksdev.onlymap.R
import com.felicksdev.onlymap.data.models.otpModels.routing.Itinerary
import com.felicksdev.onlymap.ui.common.rememberScaledWidth
import com.felicksdev.onlymap.ui.utils.rememberMapPadding
import com.felicksdev.onlymap.utils.MapConfig
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState

private val MapUiOffsetLimit = 100.dp


@Composable
fun MyMap(
    bottomPadding: Dp = 0.dp,
    layoutHeight: Dp = Dp.Unspecified,
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    mapConfiguration: MapProperties = MapConfig.mapProperties,
    mapUiConfiguration: MapUiSettings = MapConfig.mapUiConfig,
    padding: PaddingValues = PaddingValues(0.dp),
    markers: @Composable () -> Unit = {},
    itinerary: Itinerary? = null,
    isPlacesDefined: Boolean = false
) {
    val scaledWidth = rememberScaledWidth(cameraPositionState.position.zoom)
    val maxBottomPadding = remember(layoutHeight) { layoutHeight - MapUiOffsetLimit }
    val mapPadding = rememberMapPadding(bottomPadding, maxBottomPadding)

    Log.d("MyMap", "Padding: $isPlacesDefined, ScaledWidth: $scaledWidth")

    LaunchedEffect(itinerary) {
        adjustCameraToItinerary(itinerary, cameraPositionState)
    }

    GoogleMap(
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiConfiguration,
        properties = mapConfiguration,
        contentPadding = mapPadding
    ) {
        // Dibujar polilíneas con icono en la mitad
        itinerary?.let { drawItineraryWithMidIcon(it, scaledWidth) }

        // Renderizar los marcadores personalizados
        markers()
    }
}

/**
 * Dibuja las polilíneas del itinerario y coloca un ícono en la mitad de cada segmento (`Leg`).
 */
@Composable
private fun drawItineraryWithMidIcon(itinerary: Itinerary, scaledWidth: Float) {
    val context = LocalContext.current

    itinerary.legs.forEach { leg ->
        val polylinePoints = PolyUtil.decode(leg.legGeometry.points)

        // Definir color de la polilínea según el modo de transporte
        val polylineColor = when (leg.mode) {
            "BUS" -> Color.Red
            "WALK" -> Color.Gray
            else -> Color.Blue
        }

        // Dibujar la línea en el mapa
        Polyline(
            points = polylinePoints,
            color = polylineColor,
            width = scaledWidth
        )

        // Obtener el punto medio del `Polyline`
        val midPoint = getMidPoint(polylinePoints)

        // Obtener el ícono correspondiente según el modo de transporte
        val iconBitmap = remember { getTransportIcon(context, leg.mode) }

        // Dibujar el ícono en la mitad del `Polyline`
        Marker(
            state = rememberMarkerState(position = midPoint),
            icon = BitmapDescriptorFactory.fromBitmap(iconBitmap),
            title = leg.mode
        )
    }
}


/**
 * Obtiene el punto medio de una lista de coordenadas.
 */
private fun getMidPoint(points: List<LatLng>): LatLng {
    return if (points.size < 2) {
        points.firstOrNull() ?: LatLng(0.0, 0.0) // En caso de error, devolver (0,0)
    } else {
        val middleIndex = points.size / 2
        points[middleIndex] // Tomamos el punto en el centro de la lista
    }
}

/**
 * Obtiene el ícono adecuado según el tipo de transporte.
 */
private fun getTransportIcon(context: Context, mode: String): Bitmap {
    val drawableRes = when (mode) {
        "BUS" -> R.drawable.ic_bus_ios_17 // Reemplazar con el recurso real
        "WALK" -> R.drawable.ic_walk_ios_17 // Reemplazar con el recurso real
        else -> R.drawable.ic_bus_ios_17 // Ícono por defecto
    }

    val drawable = ContextCompat.getDrawable(context, drawableRes) ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}

/*
 * Ajusta la cámara para que encuadre toda la ruta del itinerario.
 */
private suspend fun adjustCameraToItinerary(itinerary: Itinerary?, cameraPositionState: CameraPositionState) {
    itinerary?.let {
        try {
            val boundsBuilder = LatLngBounds.Builder()
            it.legs.forEach { leg ->
                PolyUtil.decode(leg.legGeometry.points).forEach { point ->
                    boundsBuilder.include(point)
                }
            }
            val bounds = boundsBuilder.build()

            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngBounds(bounds, 80)
            )
        } catch (e: IllegalStateException) {
            Log.e("MyMap", "Error ajustando la cámara: ${e.localizedMessage}")
        }
    }
}

/**
 * Dibuja las polilíneas del itinerario en el mapa.
 */
@Composable
private fun DrawItinerary(itinerary: Itinerary, scaledWidth: Float) {
    itinerary.legs.forEach { leg ->
        val polylinePoints = PolyUtil.decode(leg.legGeometry.points)
        val polylineColor = when (leg.mode) {
            "BUS" -> Color.Red
            "WALK" -> Color.Gray
            else -> Color.Blue
        }

        Polyline(
            points = polylinePoints,
            color = polylineColor,
            width = scaledWidth
        )
    }
}

@Preview
@Composable
private fun MuMapPreview() {
    MyMap(cameraPositionState = MapConfig.initialState)
}