package com.felicksdev.onlymap.screens


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.felicksdev.onlymap.navigation.Destinations.*
import com.felicksdev.onlymap.utils.MapConfig
import com.felicksdev.onlymap.utils.MapUiConfig
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel, // Asume que se pasa como parámetro
    navController: NavController,
    locationViewModel: LocationViewModel
) {
    val locationLaPaz = LatLng(-16.5000000,  -68.1500000)
    val datosCargados = remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf("") }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true
            )
        )
    }
        //locationViewModel.getInitLocation(context = LocalContext.current);
    //val currentLocation: LatLng by locationViewModel.currentLocation.observeAsState(initial = locationLaPaz)
    //Log.d("HomeScreen", "Current location: ${currentLocation}")
    //val polygonPoints = remember { mutableStateOf(emptyList<LatLng>()) }
    LaunchedEffect(viewModel) {
        viewModel.loadRouteById(893)
    }

    // Campo de texto en medio del mapa


    // Mapa
    MyGoogleMap(
        mapConfiguration = MapConfig().mapProperties,
        mapUiConfiguration = MapConfig().mapUiConfig,
        initialState = MapConfig().initialState
    )
    TextField(
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            // works like onClick
                            Log.d("HomeScreen", "Click")
                            //navegarPantalla2("Hola")
                            navController.navigate(LocationsSelectionScreen.route)
                        }
                    }
                }
            },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = null)
        },
        value = textValue,
        onValueChange = {
            textValue = it
        },
        label = { Text("¿A dónde quieres ir?") },
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
            .clickable {
                //navController.navigate(LocationsSelectionScreen.route)
            }


    )

    LaunchedEffect(viewModel.rutaData.value) {
        Log.d("HomeScreen", "Ruta: ${viewModel.rutaData}")
        viewModel.rutaData?.let { ruta ->
            //val newPoints = ruta.value?.geometria_ruta!!.coordinates.flatMap { it.map { LatLng(it[1], it[0]) } }
            //polygonPoints.value = newPoints
        }
    }

}

@Composable
fun MyGoogleMap( mapConfiguration : MapProperties , mapUiConfiguration: MapUiSettings , initialState: MapConfig.CameraState){
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialState.center, initialState.zoomLevel)
        //Log.d("HomeScreen", "Camera position: ${position}")
    }


    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),  // Usa el modificador weight para ocupar el espacio restante
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiConfiguration,
        properties = mapConfiguration
    ) {

    }
}



