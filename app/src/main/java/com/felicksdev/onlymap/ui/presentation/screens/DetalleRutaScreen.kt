package com.felicksdev.onlymap.ui.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.data.models.otpModels.RouteStopItem
import com.felicksdev.onlymap.data.models.otpModels.routes.PatternGeometry
import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.ui.presentation.components.RouteDetailsTopBar
import com.felicksdev.onlymap.utils.MapConfig
import com.felicksdev.onlymap.viewmodel.RoutesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

const val TAG = "RouteDetailScreen"

@Composable
fun DetalleRutaScreen(
    idRuta: String,
    viewModel: RoutesViewModel = hiltViewModel(),
    navController: NavController
) {

    Log.d(TAG, "Route en el viewmodel: $idRuta")

    val patterGeom by viewModel.selectedPatternGeometry.collectAsState()
    val cameraPositionState by viewModel.cameraPosition.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getRouteDetails(idRuta)
        viewModel.getRouteStops(idRuta)
        viewModel.getRouteGeometry(idRuta)
    }

    val route by viewModel.routeSelected.collectAsState()
    val stopsList by viewModel.routeSelectePattern.collectAsState()
    Scaffold(
        topBar = {
            RouteDetailsTopBar(
                route = route,
                navController = navController
            )
        },
        content = { padding ->
            RouteDetailScreenContent(
                cameraPositionState = cameraPositionState,
                patternGeom = patterGeom,
                route = route,
                stopsList = stopsList.stops,
                padding = padding
            )
        }
    )
}

@Composable
fun RouteDetailScreenContent(
    route: RoutesItem,
    stopsList: List<RouteStopItem>,
    padding: PaddingValues,
    patternGeom: PatternGeometry,
    cameraPositionState: CameraPosition
) {

    val points = PolyUtil.decode(patternGeom.points)

    val cameraState = rememberCameraPositionState {
        position = cameraPositionState
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Text(
            text = "Ruta: ${route.shortName}",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Duración: 55 minutos", // Este valor debería venir de los datos correctos
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "Distancia: 5 km", // Este valor también debe ser dinámico
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))
        Map(
            stops = stopsList,
            mapConfiguration = MapConfig.mapProperties,
            mapUiConfiguration = MapConfig.mapUiConfig,
            initialState = cameraState,
            points = points
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Aquí podrías agregar una lista de paradas u otra información adicional
//        LazyColumn {
//            items(stopsList) { stop ->
//                ParadaItem(stop)
//            }
//        }
    }
}

@Composable
fun Map(
    stops: List<RouteStopItem>,
    mapConfiguration: MapProperties,
    mapUiConfiguration: MapUiSettings,
    initialState: CameraPositionState,
    points: List<LatLng>
) {
    // 🔥 Obtener el nivel de zoom actual desde CameraPositionState
    val zoom = initialState.position.zoom

    // 🔥 Parámetros clave para modificar el efecto de escalado de la línea
    val baseWidth = 5f       // ⚙️ Grosor base de la línea en zoom intermedio
    val zoomFactor = 10f     // ⚙️ Factor para suavizar el cambio de grosor según el zoom
    val minWidth =
        13f        // ⚙️ Grosor mínimo permitido (ajustar si la línea es muy fina en zoom bajo)
    val maxWidth =
        23f       // ⚙️ Grosor máximo permitido (ajustar si la línea es demasiado gruesa en zoom alto)

// 🔥 Calcular el grosor de la Polyline basado en el zoom actual
    val scaledWidth = (baseWidth * (zoom / zoomFactor)).coerceIn(minWidth, maxWidth)

    LaunchedEffect(points) {

        try {
            // Crea los límites (LatLngBounds) a partir de la lista de puntos
            val boundsBuilder = LatLngBounds.Builder()
            points.forEach { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()

            // Ejecuta la animación de la cámara en una corrutina dentro de LaunchedEffect
            initialState.animate(
                update = CameraUpdateFactory.newLatLngBounds(bounds, 80), // 50 es el padding
                //                durationMs = 1000 // Duración de la animación en milisegundos
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace() // Maneja el error para evitar crasheos
        }

    }


    GoogleMap(
        uiSettings = mapUiConfiguration,
        properties = mapConfiguration,
        cameraPositionState = initialState,
        onMapLoaded = {}
    ) {
        Polyline(
            points = points,
            color = Color.Red,
            width = scaledWidth
        )
    }
}

@Composable
fun ParadaItem(stop: RouteStopItem) {
    Row(modifier = Modifier.padding(8.dp)) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Parada",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = stop.name, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRouteDetailScreen() {
    // Datos de prueba para la ruta
    val fakeRoute = RoutesItem(
        id = "1",
        shortName = "R1",
        longName = "Ruta 1 - Centro",
        mode = "BUS",
        agencyName = "Transporte publico"
    )


    DetalleRutaScreen(
        idRuta = "1",
        navController = rememberNavController()
    )
}