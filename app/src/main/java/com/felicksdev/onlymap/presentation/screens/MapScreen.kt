package com.felicksdev.onlymap.presentation.screens

import RutasViewModel
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.felicksdev.onlymap.R
import com.felicksdev.onlymap.data.models.AddressState
import com.felicksdev.onlymap.data.models.LocationInfo
import com.felicksdev.onlymap.presentation.components.MyMap
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    viewModel: LocationViewModel,
    rutasViewModel: RutasViewModel,
    cameraPositionState: CameraPositionState
) {
    MapContent(
        viewModel = viewModel,
        cameraPositionState = cameraPositionState
    )
    Column {
        Text(text = "Origen: ${viewModel.originLocationState.value!!.address}")
    }
}

@Composable
fun MapContent(
    viewModel: LocationViewModel,
    cameraPositionState: CameraPositionState
) {


    val startLocationState by viewModel.startLocation.collectAsState()
    val endLocationState by viewModel.endLocation.collectAsState()


//    val currentLocation = viewModel.getSelectedLocationInfo()
    //    val cameraPositionState = rememberCameraPositionState {
    //        position = CameraPosition.fromLatLngZoom(initialState.position.target, 12f)
    //    }

//        val currentLocationState: AddressState = if (viewModel.destinoFieldSelected.value!!) {
//            viewModel.destinationLocationState.value
//        } else {
//            viewModel.originLocationState.value
//        }


    val currentLocationState: LocationInfo = if (viewModel.destinoFieldSelected.value!!) {
        endLocationState
    } else {
        startLocationState
    }

//    val currentLocationState: LocationInfo = if (viewModel.destinoFieldSelected.value!!) {
//        viewModel.endLocation.value ?: LocationInfo() // Proporciona un valor predeterminado
//    } else {
//        viewModel.startLocation.value ?: LocationInfo() // Proporciona un valor predeterminado
//    }


//        val currentLocationState = viewModel.getSelectedLocationInfo()

//    Log.d("MapScreen", "El estado seleccionado es $currentLocationState")

    val markerIcon = getBitmapDescriptor(LocalContext.current, R.drawable.ic_map_marker)
    val markerState = remember { MarkerState(position = cameraPositionState.position.target) }


//    LaunchedEffect(cameraPositionState.isMoving) {
//        if (!cameraPositionState.isMoving) {
//            withContext(Dispatchers.IO) {
//                val address = viewModel.getAddressByLatLng(cameraPositionState.position.target)
//                currentLocationState.address = address
//                currentLocationState.coordinates = cameraPositionState.position.target
//
//                viewModel.updateLocationInfo(
////                        isDestino = viewModel.destinoFieldSelected.value!!,
//                    locationInfo = currentLocationState
//                )
//            }
//        }
//    }
    val coroutineScope = rememberCoroutineScope()
    var cameraMoving by remember { mutableStateOf(cameraPositionState.isMoving) }
//    var lastKnownPosition by remember { mutableStateOf(cameraPositionState.position.target) }
    
    var cameraPosition by remember  { mutableStateOf(cameraPositionState.position.target) }


    LaunchedEffect(cameraPositionState.isMoving) {
        // Actualiza el estado de movimiento de la cámara
        cameraMoving = cameraPositionState.isMoving
        cameraPosition = cameraPositionState.position.target

        if (!cameraMoving) {
            // Espera un corto período de tiempo antes de hacer la llamada
//            delay(500) // Espera 500 milisegundos (puedes ajustar este tiempo según tus necesidades)

            // Verifica nuevamente si la cámara sigue sin moverse
            if (!cameraPositionState.isMoving) {
                coroutineScope.launch {
                    val address = viewModel.getAddressByLatLng(cameraPosition)
                    // Actualiza el estado de la dirección en el ViewModel
                    currentLocationState.address = address

//                      viewModel.updateLocationInfo(
//                        locationInfo = currentLocationState.copy( address= address)
//                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MyMap(
            cameraPositionState = cameraPositionState
        ) {

            // Mostrar el marcador siempre y cambiar su visibilidad según el estado de movimiento de la cámara
            Marker(
                state = MarkerState(position = cameraPosition),
                icon = markerIcon,
                visible = !cameraPositionState.isMoving
            )
            Marker(
                state = MarkerState(position = startLocationState.coordinates),
                icon = markerIcon,
                title = "Destino"
            )
            Marker(
                state = MarkerState(position = endLocationState.coordinates),
                icon = markerIcon,
                title = "Origen"
            )
            // Verifica si la coordenada del marcador está presente (no es nula)
            //            if (viewModel.origenFieldSelected.value == true && originLocationState.coordinates != null) {
            //                Marker(
            //                    state = MarkerState(position = originLocationState.coordinates!!),
            //                    icon = markerIcon,
            //                    title = "Origen"
            //                )
            //            }
        }
        // Texto en el centro del mapa mostrando la posición de la cámara
        Text(
            text = "Camera Position: ${cameraPosition.latitude}, ${cameraPosition.longitude}",
            modifier = Modifier.align(Alignment.Center)
        )


        // Interfaz de usuario adicional, como un botón y un campo de texto
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
//                        viewModel.setMapMarkerLocation(
//                            currentLocation = cameraPosition
//                        )
                        Log.d("MapScreen", "Ubicación seleccionada actaulzaido al view model: $cameraPosition")
//                        viewModel.updateLocationInfo(
//                            locationInfo = currentLocationState.copy( address= address)
//                        )
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .wrapContentHeight()
                ) {
                    Text(text = "Seleccionar ubicación")
                }
                Spacer(modifier = Modifier.weight(1f))
                TextField(
                    enabled = false,
                    value = currentLocationState.address,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 56.dp, start = 50.dp, end = 50.dp)
                )
            }
        }
    }
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

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    val mockLocationViewModel = LocationViewModel().apply {
        originLocationState.value = AddressState("Mock Origin", LatLng(0.0, 0.0))
        destinationLocationState.value = AddressState("Mock Destination", LatLng(0.0, 0.0))
    }
    val mockRutasViewModel = RutasViewModel()
    val mockCameraPositionState = CameraPositionState()

    MapScreen(
        viewModel = mockLocationViewModel,
        rutasViewModel = mockRutasViewModel,
        cameraPositionState = mockCameraPositionState
    )
}