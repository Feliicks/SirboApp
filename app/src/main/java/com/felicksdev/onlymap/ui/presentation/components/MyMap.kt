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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.felicksdev.onlymap.R
import com.felicksdev.onlymap.data.models.TransportMode
import com.felicksdev.onlymap.data.models.getColor
import com.felicksdev.onlymap.data.models.otpModels.routing.Itinerary
import com.felicksdev.onlymap.ui.common.rememberScaledWidth
import com.felicksdev.onlymap.ui.utils.rememberMapPadding
import com.felicksdev.onlymap.utils.MapConfig
import com.felicksdev.onlymap.utils.StringUtils.toTransportMode
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PatternItem
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.delay

private val MapUiOffsetLimit = 100.dp

fun adjustCameraToItineraryList(
    itineraries: List<Itinerary>,
    cameraPositionState: CameraPositionState
) {
    val allPoints = itineraries.flatMap { itinerary ->
        itinerary.legs.flatMap { leg ->
            PolyUtil.decode(leg.legGeometry.points) // Decodificar todas las coordenadas
        }
    }

    if (allPoints.isNotEmpty()) {
        val bounds = LatLngBounds.builder()
        allPoints.forEach { bounds.include(it) }

        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100))
    }
}


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
    listItinerary: List<Itinerary>? = emptyList(),
    itinerarySelected: Itinerary? = null,
    isPlacesDefined: Boolean = false
) {
    val scaledWidth = rememberScaledWidth(cameraPositionState.position.zoom)
    val maxBottomPadding = remember(layoutHeight) { layoutHeight - MapUiOffsetLimit }
    val mapPadding = rememberMapPadding(bottomPadding, maxBottomPadding)

    Log.d("MyMap", "Padding: $isPlacesDefined, ScaledWidth: $scaledWidth")

    LaunchedEffect(listItinerary) {
        Log.d("MyMap", "Ajustando a lsita de Itinerary ")
        listItinerary?.let { it ->
            adjustCameraToItineraryList(it, cameraPositionState)
        }
    }

    LaunchedEffect(itinerarySelected) {
        Log.d("MyMap", "Ajustando a Itinerary seleccionado ")
        adjustCameraToItinerary(itinerarySelected, cameraPositionState)
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
        if (!listItinerary.isNullOrEmpty()) {
            drawItineraryWithMidIcon(
                itineraries = listItinerary,
                selectedItinerary = itinerarySelected,
                scaledWidth = scaledWidth
            )
        }

        // Renderizar los marcadores personalizados
        markers()
    }
}


/**
 * Dibuja las polilíneas del itinerario y coloca un ícono en la mitad de cada segmento (`Leg`).
 */
@Composable
private fun drawItineraryWithMidIcon(
    itineraries: List<Itinerary>,
    selectedItinerary: Itinerary?,
    scaledWidth: Float
) {
    val context = LocalContext.current

    // 📌 Dibujar primero los itinerarios NO seleccionados en color gris
    itineraries.filter { it != selectedItinerary }.forEach { itinerary ->
        drawPolylineAndIcons(itinerary, context, scaledWidth, isSelected = false)
    }

    // 📌 Luego dibujar el itinerario seleccionado por encima con su color original
    selectedItinerary?.let {
        drawPolylineAndIcons(it, context, scaledWidth, isSelected = true)
    }
}

@Composable
private fun drawPolylineAndIcons(
    itinerary: Itinerary,
    context: Context,
    scaledWidth: Float,
    isSelected: Boolean
) {
    itinerary.legs.forEach { leg ->
        val polylinePoints = PolyUtil.decode(leg.legGeometry.points)

        // 📌 Si no es el seleccionado, usar gris
        val polylineColor = if (isSelected) {
            leg.mode.toTransportMode().getColor() // Color normal si está seleccionado
        } else {
            Color.Gray.copy(alpha = 0.6f) // Color gris con transparencia para rutas no seleccionadas
        }

        // 📌 Definir patrón de línea segmentada para "WALK"
        val polylinePattern = if (leg.mode.toTransportMode() == TransportMode.WALK) {
            listOf(Dash(20f), Gap(10f)) // Línea discontinua para caminar
        } else {
            null // Línea sólida para otros modos
        }

        // 📌 Dibujar la línea en el mapa
        AnimatedPolyline(
            points = polylinePoints,
            color = polylineColor,
            width = scaledWidth + if (isSelected) 2 else 0, // Aumentar grosor si es seleccionado
            pattern = polylinePattern
        )

        // 📌 Obtener el punto medio del `Polyline`
        val midPoint = getMidPoint(polylinePoints)

        // 📌 Obtener el ícono correspondiente según el modo de transporte
        val iconBitmap = remember { getTransportIcon(context, leg.mode) }

        // 📌 Dibujar el ícono en la mitad del `Polyline`
        Marker(
            state = rememberMarkerState(position = midPoint),
            icon = BitmapDescriptorFactory.fromBitmap(iconBitmap),
            title = leg.mode
        )
    }
}


@Composable
fun AnimatedPolyline(
    points: List<LatLng>,
    color: Color,
    width: Float,
    pattern: List<PatternItem>? = null,
    duration: Long = 500L // duración total de la animación
) {
    var animatedPoints by remember { mutableStateOf(listOf<LatLng>()) }

    LaunchedEffect(points) {
        animatedPoints = emptyList()
        val step = if (points.isNotEmpty()) (duration / points.size) else 0L
        points.forEachIndexed { i, point ->
            delay(step)
            animatedPoints = points.take(i + 1)
        }
    }

    if (animatedPoints.size >= 2) {
        Polyline(
            points = animatedPoints,
            color = color,
            width = width,
            pattern = pattern
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

    val drawable = ContextCompat.getDrawable(context, drawableRes) ?: return Bitmap.createBitmap(
        1,
        1,
        Bitmap.Config.ARGB_8888
    )

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}

/*
 * Ajusta la cámara para que encuadre toda la ruta del itinerario.
 */
private suspend fun adjustCameraToItinerary(
    itinerary: Itinerary?,
    cameraPositionState: CameraPositionState
) {
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