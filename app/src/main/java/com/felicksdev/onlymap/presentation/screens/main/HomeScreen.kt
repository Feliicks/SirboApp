package com.felicksdev.onlymap.presentation.screens.main


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.Destinations.LocationsSelectionScreen
import com.felicksdev.onlymap.presentation.components.MyMap
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.google.maps.android.compose.CameraPositionState

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    navController: NavController,
    innerPadding: PaddingValues,
    cameraPositionState: CameraPositionState
) {
//    val initialCameraPosition = MapConfig.initialState
//    val cameraPositionState = remember { initialCameraPosition }
    var textValue by remember { mutableStateOf("") }

    LaunchedEffect(viewModel) {
//        viewModel.loadRouteById(893)
    }
    MyMap(
        cameraPositionState = cameraPositionState,
        padding = innerPadding
    )
//    Logica para pasar a la siguiente pantalla al presioarn un text field
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



@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(
        viewModel = HomeScreenViewModel(), // Instancia de ViewModel de prueba
        navController = navController,
        innerPadding = PaddingValues(),
        cameraPositionState = CameraPositionState()
    )
}

