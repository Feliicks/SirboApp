package com.felicksdev.onlymap.presentation.screens

import RoutesViewModel
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
import com.felicksdev.onlymap.presentation.components.RouteDetailsTopBar
import com.felicksdev.onlymap.utils.MapConfig
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

@Composable
fun RouteDetailScreen(
    route: RoutesItem,
    viewModel: RoutesViewModel = hiltViewModel(),
    navController: NavController
) {
    val patterGeom = viewModel.selectedPatternGeometry.collectAsState()
    val points = PolyUtil.decode(patterGeom.value.points)
    val cameraPositionState = viewModel.cameraPosition.collectAsState()

    Log.d("RouteDetailScreen", "Points: $points")
    LaunchedEffect(route.id) {
        viewModel.getRouteStops(route.id)
        viewModel.getRouteGeometry(route.id)
    }

    val stopsList = viewModel.routeSelectePattern.stops

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
                stopsList = stopsList,
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
    patternGeom: State<PatternGeometry>,
    cameraPositionState: State<CameraPosition>
) {

    val points = PolyUtil.decode(patternGeom.value.points)

    val cameraState = rememberCameraPositionState {
        position = cameraPositionState.value
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
            width = 5f
        )

//        stops.forEach { stop ->
//            Marker(
//                state = MarkerState(position = LatLng(stop.lat, stop.lon)),
//                title = stop.name
//            )
//        }
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


    RouteDetailScreen(
        route = fakeRoute,
        navController = rememberNavController()
    )
}