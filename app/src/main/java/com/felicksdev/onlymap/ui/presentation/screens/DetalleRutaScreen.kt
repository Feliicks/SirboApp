package com.felicksdev.onlymap.ui.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.R
import com.felicksdev.onlymap.data.models.otpModels.RouteStopItem
import com.felicksdev.onlymap.data.models.otpModels.routes.PatternGeometry
import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.ui.presentation.components.RouteDetailsTopBar
import com.felicksdev.onlymap.ui.presentation.components.RouteStopItem
import com.felicksdev.onlymap.utils.BitMapUtils
import com.felicksdev.onlymap.utils.MapConfig
import com.felicksdev.onlymap.viewmodel.RoutesViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

const val TAG = "RouteDetailScreen"

@OptIn(ExperimentalMaterial3Api::class)
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
    // 🔹 Estado del Bottom Sheet
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.PartiallyExpanded)
    )
    val stops = stopsList.stops.map { it.name }

    // 🔹 Scaffold Principal con `TopAppBar`
    Scaffold(
        topBar = {
            RouteDetailsTopBar(route = route, navController = navController)
        }
    ) { innerPadding ->
        // 🔹 BottomSheetScaffold dentro de Scaffold
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetPeekHeight = 100.dp, // 🔥 Altura mínima cuando está colapsado
            sheetContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            sheetContent = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(
                            min = 100.dp,
                            max = (LocalConfiguration.current.screenHeightDp.dp / 2)
                        )
                ) {
                    // 🔹 Lista de Paradas
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        itemsIndexed(stops) { index, stop ->
                            RouteStopItem(
                                stopName = stop,
                                isFirst = index == 0,
                                isLast = index == stops.size - 1
                            )
                        }
                    }
                }
            }
        ) { padding ->
            RouteDetailScreenContent(
                cameraPositionState = cameraPositionState,
                patternGeom = patterGeom,
                route = route,
                stopsList = stopsList.stops,
                padding = innerPadding // 🔥 Ajustamos el padding correctamente
            )
        }
    }
}

@Composable
fun RouteDetailScreenContent(
    route: RoutesItem,
    stopsList: List<RouteStopItem>,
    padding: PaddingValues,
    patternGeom: PatternGeometry,
    cameraPositionState: CameraPosition
) {

    val points = remember(patternGeom.points) { PolyUtil.decode(patternGeom.points) }

    val cameraState = rememberCameraPositionState {
        position = cameraPositionState
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Map(
            stops = stopsList,
            mapConfiguration = MapConfig.mapProperties,
            mapUiConfiguration = MapConfig.mapUiConfig,
            initialState = cameraState,
            points = points
        )

        Spacer(modifier = Modifier.height(16.dp))
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
    val zoom = initialState.position.zoom
    val baseWidth = 5f
    val zoomFactor = 10f
    val minWidth = 13f
    val maxWidth = 23f
    val scaledWidth = (baseWidth * (zoom / zoomFactor)).coerceIn(minWidth, maxWidth)

    LaunchedEffect(points) {
        try {
            val boundsBuilder = LatLngBounds.Builder()
            points.forEach { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()
            initialState.animate(
                update = CameraUpdateFactory.newLatLngBounds(bounds, 80)
            )
        } catch (e: IllegalStateException) {
            e.printStackTrace()
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
            color = MaterialTheme.colorScheme.primary,
            width = scaledWidth
        )
        // 🔥 Agregar icono al inicio de la ruta1
        if (points.isNotEmpty()) {
            Marker(
                state = rememberMarkerState(position = points.first()), // 🔹 Primer punto
                title = "Inicio de Ruta",
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN) // 🚩 Verde
            )
        }

        // 🔥 Agregar icono al final de la ruta
        if (points.isNotEmpty()) {
            Marker(
                state = rememberMarkerState(position = points.last()), // 🔹 Último punto
                title = "Fin de Ruta",
                icon = BitMapUtils.bitmapDescriptor(
                    context = LocalContext.current,
                    vectorResId = R.drawable.ic_location_on,
                ) // 🚩 Rojo
            )
        }
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