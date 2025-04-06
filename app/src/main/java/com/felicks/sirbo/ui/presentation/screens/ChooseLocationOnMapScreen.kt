package com.felicks.sirbo.ui.presentation.screens

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
import com.felicks.sirbo.LocationDetail
import com.felicks.sirbo.R
import com.felicks.sirbo.domain.LocationProperties
import com.felicks.sirbo.ui.navigation.Destinations
import com.felicks.sirbo.ui.presentation.components.MyMap
import com.felicks.sirbo.ui.presentation.components.bottomBars.BottomSearchBar
import com.felicks.sirbo.ui.presentation.components.topBars.ChooseLocationOnMapTopBar
import com.felicks.sirbo.viewmodel.LocationViewModel
import com.felicks.sirbo.viewmodel.PlannerViewModel
import com.google.android.gms.maps.model.LatLng
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
    navController: NavController,
    plannerViewModel: PlannerViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel(),
    cameraPositionState: CameraPositionState
) {
    val coroutineScope = rememberCoroutineScope()
    var debouncedLatLng by remember { mutableStateOf(cameraPositionState.position.target) }
    val currentLocation by locationViewModel.currentAddress.collectAsState()
    val isPlacesDefined = plannerViewModel.isPlacesDefined()

    HandleCameraPositionChange(
        cameraPositionState = cameraPositionState,
        onPositionSettled = { newLatLng ->
            debouncedLatLng = newLatLng
            locationViewModel.getAddress(newLatLng)
        }
    )

    Scaffold(
        topBar = {
            ChooseLocationOnMapTopBar(navController = navController)
        },
        bottomBar = {
            LocationBottomSearchBar(
                isOrigin = isOrigin,
                location = currentLocation,
                latLng = debouncedLatLng,
                onConfirm = {
                    handleLocationConfirm(
                        isOrigin = isOrigin,
                        latLng = debouncedLatLng,
                        address = currentLocation,
                        plannerViewModel = plannerViewModel,
                        navController = navController
                    )
                }
            )
        }
    ) { padding ->
        ChooseLocationOnMapScreenContent(
            viewModel = locationViewModel,
            padding = padding,
            cameraPositionState = cameraPositionState,
            isPlacesDefined = isPlacesDefined
        )
    }
}




@Composable
fun LocationBottomSearchBar(
    isOrigin: Boolean,
    location: LocationProperties,
    latLng: LatLng,
    onConfirm: () -> Unit
) {
    val address = location.street ?: listOfNotNull(
        location.name,
        location.district,
        location.state
    ).joinToString(", ")

    BottomSearchBar(
        isOrigin = isOrigin,
        address = address,
        latLng = latLng,
        onConfirm = onConfirm
    )
}


@Composable
fun HandleCameraPositionChange(
    cameraPositionState: CameraPositionState,
    onPositionSettled: (LatLng) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    delay(500)
                }
                onPositionSettled(cameraPositionState.position.target)
            }
        }
    }
}

fun handleLocationConfirm(
    isOrigin: Boolean,
    latLng: LatLng,
    address: LocationProperties,
    plannerViewModel: PlannerViewModel,
    navController: NavController
) {
    val description = address.street ?: listOfNotNull(
        address.name,
        address.district,
        address.state
    ).joinToString(", ")

    val location = LocationDetail(
        description = description,
        latitude = latLng.latitude,
        longitude = latLng.longitude
    )

    if (isOrigin) {
        plannerViewModel.setFromPlace(location)
    } else {
        plannerViewModel.setToPlace(location)
    }

    val isPlacesDefined = plannerViewModel.isPlacesDefined()
    Log.d("ChooseLocationsScreen", "is places test defined: $isPlacesDefined")

    navController.navigate(Destinations.HomeScreen.route) {
        popUpTo(Destinations.HomeScreen.route) { inclusive = true }
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
        locationViewModel = hiltViewModel(),
        plannerViewModel = hiltViewModel(),
        cameraPositionState = CameraPositionState(),
        navController = rememberNavController(),
    )
}

