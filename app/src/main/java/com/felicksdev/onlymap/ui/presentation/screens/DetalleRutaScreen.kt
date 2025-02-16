package com.felicksdev.onlymap.ui.presentation.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
import io.morfly.compose.bottomsheet.material3.BottomSheetScaffold
import io.morfly.compose.bottomsheet.material3.layoutHeightDp
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetState
import io.morfly.compose.bottomsheet.material3.requireSheetVisibleHeightDp

const val TAG = "RouteDetailScreen"


private const val DefaultMapZoom = 13f
private val MapUiOffsetLimit = 100.dp


@SuppressLint("UnusedBoxWithConstraintsScope")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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

    var isInitialState by rememberSaveable { mutableStateOf(true) }

    val bottomSheet = rememberBottomSheetState(
        initialValue = com.felicksdev.onlymap.ui.common.SheetValue.PartiallyExpanded,
        defineValues = {
            // Bottom sheet height is 100 dp.
            com.felicksdev.onlymap.ui.common.SheetValue.Collapsed at  height(100.dp)
            if (isInitialState) {
                // Offset is 60% which means the bottom sheet takes 40% of the screen.
                com.felicksdev.onlymap.ui.common.SheetValue.PartiallyExpanded at offset(percent = 60)
            }
            // Bottom sheet height is equal to the height of its content.
            com.felicksdev.onlymap.ui.common.SheetValue.Expanded at contentHeight
        },
        confirmValueChange = {
            if (isInitialState) {
                isInitialState = false
                refreshValues()
            }
            true
        }
    )
    val scaffoldState =
        io.morfly.compose.bottomsheet.material3.rememberBottomSheetScaffoldState(bottomSheet)




        Scaffold(
            topBar = {
                RouteDetailsTopBar(route = route, navController = navController)
            }
        ) { innerPadding ->
            // 🔹 BottomSheetScaffold dentro de Scaffold
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                sheetContent = {
                    if (true) {
                        BottomSheetContent(stops)
                    }
                }
            ) { it ->
                val bottomPadding by remember {
                    derivedStateOf { bottomSheet.requireSheetVisibleHeightDp() }
                }
                RouteDetailScreenContent(
                    bottomPadding = bottomPadding,
                    cameraPositionState = cameraPositionState,
                    patternGeom = patterGeom,
                    route = route,
                    stopsList = stopsList.stops,
                    padding = innerPadding, // 🔥 Ajustamos el padding correctamente
                    layoutHeight = bottomSheet.layoutHeightDp
                )
                Log.d("CustomFinalizedDemoScreen", "bottom padding: ${bottomPadding}")
            }
        }

}

@Composable
private fun BottomSheetContent(stops: List<String>) {
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

@Composable
private fun rememberMapPadding(bottomPadding: Dp, maxBottomPadding: Dp): PaddingValues {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    return if (isPortrait) {
        rememberPortraitMapPadding(bottomPadding, maxBottomPadding)
    } else {
        remember { PaddingValues() }
    }
}
@Composable
private fun rememberPortraitMapPadding(bottomPadding: Dp, maxBottomPadding: Dp): PaddingValues {
    return remember(bottomPadding, maxBottomPadding) {
        PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = bottomPadding.takeIf { it < maxBottomPadding } ?: maxBottomPadding
        )
    }
}
@Composable
fun RouteDetailScreenContent(
    route: RoutesItem,
    stopsList: List<RouteStopItem>,
    padding: PaddingValues,
    patternGeom: PatternGeometry,
    cameraPositionState: CameraPosition,
    layoutHeight: Dp = Dp.Unspecified,
    bottomPadding: Dp = 0.dp,
) {
    val maxBottomPadding = remember(layoutHeight) { layoutHeight - MapUiOffsetLimit }
    val mapPadding = rememberMapPadding(bottomPadding, maxBottomPadding)

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
            points = points,
            contentPadding = mapPadding,
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
    points: List<LatLng>,
    contentPadding : PaddingValues
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
        onMapLoaded = {},
        contentPadding = contentPadding,

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