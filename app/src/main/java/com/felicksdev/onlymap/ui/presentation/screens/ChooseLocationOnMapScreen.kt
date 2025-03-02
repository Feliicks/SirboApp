package com.felicksdev.onlymap.ui.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.LocationDetail
import com.felicksdev.onlymap.R
import com.felicksdev.onlymap.ui.navigation.Destinations
import com.felicksdev.onlymap.ui.presentation.components.MyMap
import com.felicksdev.onlymap.ui.presentation.components.bottomBars.BottomSearchBar
import com.felicksdev.onlymap.ui.presentation.components.topBars.ChooseLocationOnMapTopBar
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ChooseLocationOnMapScreen(
    isOrigin: Boolean,
    viewModel: LocationViewModel,
    cameraPositionState: CameraPositionState,
    navController: NavController,
    plannerViewModel: PlannerViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel()
) {
//    val state by plannerViewModel.plannerState.collectAsState()
    var debouncedLatLng by remember { mutableStateOf(cameraPositionState.position.target) }
    val coroutineScope = rememberCoroutineScope()
    val currenLocation by locationViewModel.currentAddress.collectAsState()
    val isPlacesDefined = plannerViewModel.isPlacesDefined()
//    val currentLocation b
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    delay(500) // Espera en un hilo secundario
                }
                debouncedLatLng =
                    cameraPositionState.position.target // Actualiza el estado en el hilo principal
                locationViewModel.getAddress(debouncedLatLng)
            }
        }
    }
    Scaffold(
        topBar = { ChooseLocationOnMapTopBar(navController = navController) },
        bottomBar = {
            BottomSearchBar(
                isOrigin = isOrigin,
                address = (if (currenLocation.street != null) {
                    currenLocation.street
                } else {
                    listOfNotNull(
                        currenLocation.name,
                        currenLocation.district,
                        currenLocation.state
                    )
                        .joinToString(", ")
                }).toString(),
                latLng = debouncedLatLng,
                onConfirm = {
                    val location = LocationDetail(
                        description = if (isOrigin) "Origin Map" else "Destination Map",
                        latitude = debouncedLatLng.latitude,
                        longitude = debouncedLatLng.longitude
                    )
                    if (isOrigin) {
                        plannerViewModel.setFromPlace(location)
                    } else {
                        plannerViewModel.setToPlace(location)
                    }

                    // Verifica si ambos lugares están definidos
                    val isPlacesDefined = plannerViewModel.isPlacesDefined()
                    Log.d(
                        "ChooseLocationsScreen",
                        "is places test defined: ${plannerViewModel.isPlacesDefined()}"
                    )

                    // Navega según el estado de isPlacesDefined
                    if (isPlacesDefined) {
                        navController.navigate(Destinations.HomeScreen.route) {
                            popUpTo(Destinations.HomeScreen.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Destinations.HomeScreen.route) {
                            popUpTo(Destinations.HomeScreen.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    ) { padding ->
        ChooseLocationOnMapScreenContent(
            viewModel = viewModel,
            padding = padding,
            cameraPositionState = cameraPositionState,
            isPlacesDefined = isPlacesDefined
        )
    }
}

@Composable
private fun ChooseLocationOnMapScreenContent(
    viewModel: LocationViewModel,
    padding: PaddingValues,
    cameraPositionState: CameraPositionState,
    isPlacesDefined: Boolean
) {
    MapContent(
        viewModel = viewModel,
        padding = padding,
        cameraPositionState = cameraPositionState,
        isPlacesDefined = isPlacesDefined
    )
    Column {
        Text(text = "Origen: ${viewModel.originLocationState.value.address}")
    }
}

@Composable
fun MapContent(
    viewModel: LocationViewModel,
    padding: PaddingValues,
    cameraPositionState: CameraPositionState,
    isPlacesDefined: Boolean,
) {
    val myCameraPositionState = remember { cameraPositionState }
    val startLocationState by viewModel.startLocation.collectAsState()
    val endLocationState by viewModel.endLocation.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    var cameraMoving by remember { mutableStateOf(cameraPositionState.isMoving) }
    var cameraPosition by remember { mutableStateOf(cameraPositionState.position.target) }

    LaunchedEffect(cameraPositionState.isMoving) {
        cameraMoving = cameraPositionState.isMoving
        cameraPosition = cameraPositionState.position.target
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        MyMap(
            isPlacesDefined = isPlacesDefined,
            cameraPositionState = myCameraPositionState,
            markers = {
                Marker(
                    state = MarkerState(position = startLocationState.coordinates),

                    title = "Origen"
                )
                Marker(
                    state = MarkerState(position = endLocationState.coordinates),

                    title = "Destino"
                )
            }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_map_marker),
                contentDescription = "marker",
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMapScreen() {
    ChooseLocationOnMapScreen(
        isOrigin = true,
        viewModel = hiltViewModel(),
        cameraPositionState = CameraPositionState(),
        navController = rememberNavController(),
    )
}

