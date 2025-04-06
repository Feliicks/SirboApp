package com.felicks.sirbo.ui.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicks.sirbo.data.models.AddressState
import com.felicks.sirbo.data.models.otpModels.routing.Leg
import com.felicks.sirbo.viewmodel.LocationViewModel
import com.felicks.sirbo.viewmodel.MainViewModel
import com.felicks.sirbo.viewmodel.RoutesViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Deprecated("Ya no se usa esta pantalla")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimalRoutesScreen(
    mainViewModel: MainViewModel,
    locationViewModel: LocationViewModel,
    routesViewModel: RoutesViewModel = hiltViewModel(),
    navController: NavController,
) {
    val errorState by routesViewModel.errorState.collectAsState()
    Log.d("MapScreen", "El estado de error es ${errorState}")
    val scope = rememberCoroutineScope()
    val isSheetOpen = rememberSaveable {
        mutableStateOf(false)
    }
    val scaffoldState = rememberBottomSheetScaffoldState()

    val originLocation: AddressState = locationViewModel.originLocationState.value!!
    val destinationLocation: AddressState = locationViewModel.destinationLocationState.value!!
    Log.d("OptimalRoutesScreen", "originLocation: $originLocation")
    Log.d("OptimalRoutesScreen", "destinationLocation: $destinationLocation")
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(destinationLocation.coordinates, 10f)
    }
    var optimalRoutesLeg by remember { mutableStateOf(emptyList<Leg>()) }

    LaunchedEffect(key1 = originLocation) {
        //Hacer peticion al servidor yguradar lista de rutas en ajá
        routesViewModel.getOptimalRoutes(originLocation, destinationLocation)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
//            .padding(paddingValues)
    ) {


        GoogleMap(
            modifier = Modifier
                .fillMaxWidth(),
            cameraPositionState = cameraPositionState
        ) {

            optimalRoutesLeg = routesViewModel.optimalRouteLegs
            Log.d("OptimalRoutesScreen", "Se tiene ${optimalRoutesLeg.size} legs")
            if (optimalRoutesLeg.isNotEmpty()) {
                Log.d("OptimalRoutesScreen", "optimalRoutesLeg: $optimalRoutesLeg")
/// todo corregi ebn punto de desitno se slecicon automaticamen cuando se reuqer selecciona el mapa. debe presoanr seleccioanr el mapa para que realmente se seleccione
                optimalRoutesLeg.forEachIndexed() { index, leg ->
                    Log.d("OptimalRoutesScreen", "el ${index} leg es modo ${leg.mode}")
                    Log.d("OptimalRoutesScreen", "el ${index} leg es detaelle ${leg.legGeometry}")
                    val polylinePoints = PolyUtil.decode(leg.legGeometry.points)

                    Log.d(
                        "OptimalRoutesScreen",
                        "lista de puntos de  ${index} leg es ${polylinePoints}"
                    )

                    // Determinar el color de la polilínea según el modo de transporte
                    val polylineColor = when (leg.mode) {
                        "BUS" -> Color.Red
                        "WALK" -> Color.Gray
                        else -> Color.Blue
                    }

                    // Dibujar la polilínea con los puntos de la ruta
                    Polyline(
                        points = polylinePoints,
                        color = polylineColor,
                        width = 5f,
                    )

                    // Agregar marcadores para cada step
//                    leg.steps.forEach { step ->
//                        val latLng = LatLng(step.lat, step.lon)
//
//                        Marker(
//                            state = MarkerState(position = latLng),
//                            title = step.streetName
//                        )
//                    }
                }
            }

            Marker(
                state = MarkerState(position = originLocation.coordinates),
                title = "Origen",
                snippet = "Punto de partida"
            )
            Marker(
                state = MarkerState(position = destinationLocation.coordinates),
                title = "Destino",
                snippet = "Punto de llegada"
            )
        }
//        BottomDetail(scope, sheetState)

        // Mostrar alternativas de rutas
//        RouteAlternativesList(mapViewModel.routeAlternatives)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    isSheetOpen.value = true
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                },
                modifier = Modifier
                    .width(200.dp)
                    .wrapContentHeight()
            ) {
                Text(text = "Mostrar detalles de la ruta")
            }
            if (errorState != null) {
                AlertDialog(
                    onDismissRequest = { routesViewModel.clearError() },
                    title = { Text(text = "Error") },
                    text = {
                        Text(text = errorState ?: "Error desconocido")

                    },
                    confirmButton = {
                        Button(onClick = { routesViewModel.clearError() }) {
                            Text("OK")
                        }
                    }
                )
            }
//            BottomSheetDetail(scaffoldState = scaffoldState, optimalRoutesLeg)
        }
    }


}

@Preview(showBackground = true)
@Composable
fun PreviewOptimalRoutesScreen() {
    val navController = rememberNavController()

    OptimalRoutesScreen(
        mainViewModel = MainViewModel(),
        locationViewModel = hiltViewModel(),
        navController = navController,
    )
}


