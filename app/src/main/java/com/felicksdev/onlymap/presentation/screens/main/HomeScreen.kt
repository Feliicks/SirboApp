package com.felicksdev.onlymap.presentation.screens.main


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.felicksdev.onlymap.navigation.Destinations.LocationsSelectionScreen
import com.felicksdev.onlymap.utils.MapConfig
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel, // Asume que se pasa como parámetro
    navController: NavController,
    defaultPadding: PaddingValues
) {
    val userLocationState =
        remember { mutableStateOf(LatLng(0.0, 0.0)) } // Inicializa la ubicación en (0.0, 0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocationState.value, 17f)
    }
    var textValue by remember { mutableStateOf("") }

    LaunchedEffect(viewModel) {
        viewModel.loadRouteById(893)
    }
    MyGoogleMap(
        mapConfiguration = MapConfig().mapProperties,
        mapUiConfiguration = MapConfig().mapUiConfig,
        initialState = MapConfig().initialState,
        padding = defaultPadding
    )
    TextField(
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            Log.d("HomeScreen", "Click")
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
            }


    )

    LaunchedEffect(viewModel.rutaData.value) {
        Log.d("HomeScreen", "Ruta: ${viewModel.rutaData}")
        viewModel.rutaData?.let { ruta ->
        }
    }

}

@Composable
fun MyGoogleMap(
    mapConfiguration: MapProperties,
    mapUiConfiguration: MapUiSettings,
    initialState: CameraPositionState,
    padding: PaddingValues = PaddingValues(0.dp)
) {
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),  // Usa el modificador weight para ocupar el espacio restante
        cameraPositionState = initialState,
        uiSettings = mapUiConfiguration,
        properties = mapConfiguration

    ) {

    }
}



