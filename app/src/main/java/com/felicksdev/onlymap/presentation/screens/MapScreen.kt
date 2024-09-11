package com.felicksdev.onlymap.presentation.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.R
import com.felicksdev.onlymap.TrufiLocation
import com.felicksdev.onlymap.data.models.LocationInfo
import com.felicksdev.onlymap.navigation.Destinations
import com.felicksdev.onlymap.presentation.components.MyMap
import com.felicksdev.onlymap.presentation.components.bottomBars.BottomSearchBar
import com.felicksdev.onlymap.presentation.components.topBars.ChooseOnMapTopBar
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MapScreen(
    isOrigin: Boolean,
    viewModel: LocationViewModel,
    cameraPositionState: CameraPositionState,
    navController: NavController,
    plannerViewModel: PlannerViewModel
) {
    val state by plannerViewModel.plannerState.collectAsState()
    var debouncedLatLng by remember { mutableStateOf(cameraPositionState.position.target) }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    delay(200) // Espera en un hilo secundario
                }
                debouncedLatLng =
                    cameraPositionState.position.target // Actualiza el estado en el hilo principal
            }
        }
    }
    Scaffold(
        topBar = { ChooseOnMapTopBar(navController = navController) },
        bottomBar = {
            BottomSearchBar(
                isOrigin = isOrigin,
                address = "Avenida Ayacucho",
                latLng = debouncedLatLng,
                onConfirm = {
                    val location = TrufiLocation(
                        description = if (isOrigin) "Origin Map" else "Destination Map",
                        latitude = debouncedLatLng.latitude,
                        longitude = debouncedLatLng.longitude
                    )

                    // Actualiza el lugar en el ViewModel
                    if (isOrigin) {
                        plannerViewModel.setFromPlace(location)
                    } else {
                        plannerViewModel.setToPlace(location)
                    }

                    // Verifica si ambos lugares están definidos
                    val isPlacesDefined = plannerViewModel.isPlacesDefined()
//                    Log.d("ChooseLocationsScreen", "is places defined: $isPlacesDefined")
                    Log.d(
                        "ChooseLocationsScreen",
                        "is places test defined: ${plannerViewModel.isPlacesDefined()}"
                    )

                    // Navega según el estado de isPlacesDefined
                    if (isPlacesDefined) {
                        navController.navigate(Destinations.HomeScreen.route) {
                            popUpTo(Destinations.HomeScreen.route) { inclusive = true }
                        }
//                        navController.navigate(Destinations.OptimalRoutesScreen.route)
                    } else {
                        // Realiza popBackStack a la pantalla de inicio si los lugares no están definidos
                        navController.navigate(Destinations.HomeScreen.route) {
                            popUpTo(Destinations.HomeScreen.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    ) { padding ->
        MapContent(
            viewModel = viewModel,
            padding = padding,
            cameraPositionState = cameraPositionState,
            isPlacesDefined = state.isPlacesDefined
        )
        Column {
            Text(text = "Origen: ${viewModel.originLocationState.value!!.address}")
        }
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

    val currentLocationState: LocationInfo = if (viewModel.destinoFieldSelected.value!!) {
        endLocationState
    } else {
        startLocationState
    }

//    val markerIcon = getBitmapDescriptor(LocalContext.current, R.drawable.ic_map_marker)
    val markerState = remember { MarkerState(position = cameraPositionState.position.target) }

    val coroutineScope = rememberCoroutineScope()
    var cameraMoving by remember { mutableStateOf(cameraPositionState.isMoving) }
    var cameraPosition by remember { mutableStateOf(cameraPositionState.position.target) }

    LaunchedEffect(cameraPositionState.isMoving) {
        cameraMoving = cameraPositionState.isMoving
        cameraPosition = cameraPositionState.position.target

        if (!cameraMoving) {
            coroutineScope.launch {
                // hacer peticion con geocoder reversa
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        MyMap(
            isplacesDefined = isPlacesDefined,
            cameraPositionState = myCameraPositionState,
            markers = {
//                Marker(
//                    state = MarkerState(position = cameraPosition),
//                    icon = markerIcon,
//                    visible = !cameraPositionState.isMoving
//                )
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
//                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Spacer para empujar la imagen y el texto hacia el centro

//            Text(
//                text = "Camera is Moving: ${cameraPositionState.isMoving}",
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            Text(
//                text = "Camera Position: ${cameraPosition.latitude}, ${cameraPosition.longitude}",
//                modifier = Modifier.padding(bottom = 16.dp)
//            )

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
    MapScreen(
        isOrigin = true,
        viewModel = LocationViewModel(),
        cameraPositionState = CameraPositionState(),
        navController = rememberNavController(),
        plannerViewModel = PlannerViewModel()
    )
}

// Función para obtener el descriptor de bitmap
private fun getBitmapDescriptor(context: Context, id: Int): BitmapDescriptor? {
    val vectorDrawable: Drawable? = context.getDrawable(id)
    return vectorDrawable?.let {
        val w = it.intrinsicWidth
        val h = it.intrinsicHeight
        it.setBounds(0, 0, w, h)
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        it.draw(canvas)
        BitmapDescriptorFactory.fromBitmap(bm)
    }
}