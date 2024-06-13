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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.felicksdev.onlymap.R
import com.felicksdev.onlymap.data.models.AddressState
import com.felicksdev.onlymap.utils.MapConfig
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    viewModel: LocationViewModel,
    rutasViewModel: RutasViewModel
) {


    MapContent(
        viewModel = viewModel,
        uiSettings = MapConfig().mapUiConfig,
        properties = MapConfig().mapProperties,
        initialState = MapConfig().initialState
    )
    Column {
        Text(text = "")

    }


}

@Composable
fun MapContent(
    viewModel: LocationViewModel,
    uiSettings: MapUiSettings,
    properties: MapProperties,
    initialState: CameraPositionState
) {
    val originLocationState: AddressState = viewModel.originLocationState.value!!
    val destinoLocationState: AddressState = viewModel.destinationLocationState.value!!

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialState.position.target, 12f)
    }

    val currentLocationState: AddressState = if (viewModel.destinoFieldSelected.value!!) {
        viewModel.destinationLocationState.value
    } else {
        viewModel.originLocationState.value
    }

    Log.d("MapScreen", "El estado seleccionado es $currentLocationState")

    val markerIcon = getBitmapDescriptor(LocalContext.current, R.drawable.ic_map_marker)

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            // Añadir lógica para obtener la dirección cuando la cámara deje de moverse
            Log.d("MapScreen", "La cámara deje de moverse")
            currentLocationState.address =
                viewModel.getAddressByLatLng(cameraPositionState.position.target)
//        TODO:
//         hacerlo en un hilo secundario ay que esta relentizado la ui
//            o hacerlo directamnete cuando se presiona el boton de si

            currentLocationState.coordinates = cameraPositionState.position.target
            Log.d(
                "MapScreen",
                "el geocoder es " + viewModel.getAddressByLatLng(cameraPositionState.position.target)
            )

            if (viewModel.destinoFieldSelected.value!!) {
                viewModel.getAddressDestino(cameraPositionState.position.target)
            } else {
                viewModel.getAddressOrigen(cameraPositionState.position.target)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            uiSettings = uiSettings,
            properties = properties,
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        ) {
            // Mostrar el marcador siempre y cambiar su visibilidad según el estado de movimiento de la cámara
            Marker(
                state = MarkerState(position = cameraPositionState.position.target),
                icon = markerIcon,
                visible = !cameraPositionState.isMoving
            )
            // Verifica si la coordenada del marcador está presente (no es nula)
//            if (viewModel.destinoFieldSelected.value == true ) {
//                Marker(
//                    state = MarkerState(position = originLocationState.coordinates!!),
//                    icon = markerIcon,
//                    title = "Origen"
//                )
//            }
            Marker(
                state = MarkerState(position = destinoLocationState.coordinates!!),
                icon = markerIcon,
                title = "Destino"
            )
            Marker(
                state = MarkerState(position = originLocationState.coordinates!!),
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
                        if (viewModel.destinoFieldSelected.value!!) {
                            viewModel.destinationLocationState.value = currentLocationState
                            Log.d(
                                "MapScreen",
                                "se ha puesto la direccion actual en el destino ${viewModel.destinationLocationState.value}"
                            )
                        }
                        if (viewModel.origenFieldSelected.value!!) {
                            viewModel.originLocationState.value = currentLocationState
                            Log.d(
                                "MapScreen",
                                "se ha puesto la direccion actual en el origen ${viewModel.originLocationState.value}"
                            )

                        }

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
                    value = viewModel.destinoAddressText,
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

@Preview
@Composable
fun MapScreenPreview() {
    MapScreen(
        viewModel = LocationViewModel(),
        rutasViewModel = RutasViewModel()
    )
}
