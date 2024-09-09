package com.felicksdev.onlymap.presentation.screens

import RutasViewModel
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.data.models.otpModels.RouteStopItem
import com.felicksdev.onlymap.data.models.otpModels.RoutesModelItem
import com.felicksdev.onlymap.presentation.components.RouteDetailsTopBar
import com.felicksdev.onlymap.utils.MapConfig
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun RouteDetailScreen(
    route: RoutesModelItem,
    viewModel: RutasViewModel,
    navController: NavController
) {


    LaunchedEffect(route.id) {
        viewModel.getRouteStops(route.id)
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
                route = route,
                stopsList = stopsList,
                padding = padding
            )
        }
    )
}

@Composable
fun RouteDetailScreenContent(
    route: RoutesModelItem,
    stopsList: List<RouteStopItem>,
    padding: PaddingValues
) {
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
        val cameraState = rememberCameraPositionState {
            position = MapConfig.initialState.position
        }
        Map(
            stops = stopsList,
            mapConfiguration = MapConfig.mapProperties,
            mapUiConfiguration = MapConfig.mapUiConfig,
            initialState = cameraState
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
    initialState: CameraPositionState
) {
    val polyLines = stops.map { LatLng(it.lat, it.lon) }


    LaunchedEffect(polyLines) {
        if (polyLines.isNotEmpty()) {
            try {
                // Crea los límites (LatLngBounds) a partir de la lista de puntos
                val boundsBuilder = LatLngBounds.Builder()
                polyLines.forEach { boundsBuilder.include(it) }
                val bounds = boundsBuilder.build()

                // Ejecuta la animación de la cámara en una corrutina dentro de LaunchedEffect
                initialState.animate(
                    update = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                )
            } catch (e: IllegalStateException) {
                e.printStackTrace() // Maneja el error para evitar crasheos
            }
        } else {
            // Si no hay puntos, podrías mantener una cámara por defecto
            initialState.animate(
                update = CameraUpdateFactory.newLatLngZoom(LatLng(0.0, 0.0), 1f) // Ajusta según sea necesario
            )
        }
    }

    GoogleMap(
        uiSettings = mapUiConfiguration,
        properties = mapConfiguration,
        cameraPositionState = initialState,
        onMapLoaded = {}
    ) {
        Polyline(
            points = polyLines,
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
    val fakeRoute = RoutesModelItem(
        id = "1",
        shortName = "R1",
        longName = "Ruta 1 - Centro",
        mode = "BUS",
        agencyName = "Transporte publico"
    )


    RouteDetailScreen(
        route = fakeRoute,
        viewModel = RutasViewModel(),
        navController = rememberNavController()
    )
}