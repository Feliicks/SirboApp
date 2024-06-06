package com.felicksdev.onlymap.presentation.screens

import RutasViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.felicksdev.onlymap.data.models.otpModels.RouteStopItem
import com.felicksdev.onlymap.data.models.otpModels.RoutesModelItem
import com.felicksdev.onlymap.presentation.screens.components.RouteDetailsTopBar
import com.felicksdev.onlymap.utils.MapConfig
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline

@Composable
fun RouteDetailScreen(
    ruta : RoutesModelItem,
    viewModel: RutasViewModel,
    padding: PaddingValues,
//    navController: NavController
) {
//    val ruta: RoutesModelItem = viewModel.routeSelected;
    Log.d("RouteDetailScreen", "Rut" +
            "a seleccionada en e detail screen es: ${ruta}")
//    Usar un launched effect para hacer peticion del detalle de la ruta con el id que se paso porparametro
    LaunchedEffect(ruta.id) {
        Log.d("RouteDetailScreen", "ruta seleccioando como parametro es l: ${ruta}")
        viewModel.getRouteStops(ruta.id)
        Log.d("RouteDetailScreen", "RouteDetailScreen: ${viewModel.routeSelectePattern}")
    }
    var stopsList: List<RouteStopItem> = viewModel.routeSelectePattern.stops
    val polyLines = viewModel.routeStops.map { LatLng(it.lat, it.lon) }
    Log.d("RouteDetailScreen", "RouteDetailScreen: ${polyLines}")

    Column(modifier = Modifier.padding(padding)) {
        RouteDetailsTopBar(route = ruta
//            navController = navController
        )
        Text(text = "Ruta: ${ruta.shortName}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Duración: dsa5 minutos")
        Text(text = "Distancia: 5 km")

        Spacer(modifier = Modifier.height(16.dp))

        Map(
            stops = stopsList,
            mapConfiguration = MapConfig().mapProperties,
            mapUiConfiguration = MapConfig().mapUiConfig,
            initialState = MapConfig().initialState,
        )

        Spacer(modifier = Modifier.height(16.dp))

//        LazyColumn {
//            items(ruta.paradas) { parada ->
//                ParadaItem(parada)
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
) {
    val polyLines = stops.map { LatLng(it.lat, it.lon) }
    Log.d("Map", "Map: ${polyLines}")
    //    val mapState = rememberMapState()
//    val routeId = ruta.id
//    moverl ogica de datos al viewmodel

//    val mapState = remember { MapState() }
//    val cameraPosition = remember { CameraPosition(ruta.centro, 12f) }

    GoogleMap(
        uiSettings = mapUiConfiguration,
        properties = mapConfiguration,
        cameraPositionState = initialState,
    ) {
        Polyline(
            points = polyLines,
            color = Color.Red,
            width = 5f
        )
//        stops.forEach { parada ->
//            Marker(
//                state = MarkerState(position = LatLng(parada.lat, parada.lon)),
//                title = parada.name
//            )
//        }
    }
}

@Composable
fun ParadaItem(parada: RouteStopItem) {
    Row(modifier = Modifier.padding(8.dp)) {
        Image(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "Parada",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = parada.name)
            Text(text = parada.name)
        }
    }
}