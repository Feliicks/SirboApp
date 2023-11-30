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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.felicksdev.onlymap.navigation.Destinations
import com.felicksdev.onlymap.navigation.Destinations.*
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun HomeScreen(
    navegarPantalla2: (String) -> Unit,
    viewModel: HomeScreenViewModel, // Asume que se pasa como parámetro
    navController: NavController
) {
    val datosCargados = remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf("") }
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true
            )
        )
    }
    val polygonPoints = remember { mutableStateOf(emptyList<LatLng>()) }
    LaunchedEffect(viewModel) {
        viewModel.loadRouteById(893)
    }


    // Campo de texto en medio del mapa


    // Mapa
    MyGoogleMap()
    TextField(
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            // works like onClick
                            Log.d("HomeScreen", "Click")
                            //navegarPantalla2("Hola")
                            navController.navigate(Destinations.LocationsSelectionScreen.route)
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

fun openNewScreen() {
    // Crea una tarea para abrir la nueva pantalla
    //navegarPantalla2("Hola")
}

@Preview
@Composable
fun MyGoogleMap() {
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        //position = CameraPosition.fromLatLngZoom(singapore, 10f)
        position = CameraPosition.fromLatLngZoom(LatLng(37.7749, -122.4194), 10f)
    }
    var isPolygonSelected by remember { mutableStateOf(false) }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),  // Usa el modificador weight para ocupar el espacio restante
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = true)
    ) {
        // Add markers, polylines, etc. to the map.
        Polyline(
            points = listOf(
                LatLng(37.7749, -122.4194),
                LatLng(37.8049, -122.4400),
                LatLng(37.7949, -122.4100)
            ),
            clickable = true,
            tag = "San francisco",
            onClick = { polygon ->
                isPolygonSelected = true
            }
        )
    }
}



