package com.felicksdev.onlymap.presentation.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.felicksdev.onlymap.R
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.felicksdev.onlymap.utils.MapConfig
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.compose.CameraPositionState

import com.google.maps.android.compose.MapUiSettings

@Composable
fun MapScreen(
    viewModel: LocationViewModel,
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
    var markerPosition = initialState.position.target
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 12f)
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            viewModel.getAddressDestino(cameraPositionState.position.target)
        }
    }
    fun getBitmapDescriptor(context: Context, id: Int): BitmapDescriptor? {
        val vectorDrawable: Drawable?
        vectorDrawable = context.getDrawable(id)
        if (vectorDrawable != null) {
            val w = vectorDrawable.intrinsicWidth
            val h = vectorDrawable.intrinsicHeight

            vectorDrawable.setBounds(0, 0, w, h)
            val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            val canvas = Canvas(bm);
            vectorDrawable.draw(canvas);

            return BitmapDescriptorFactory.fromBitmap(bm);
        }
        return null
    }
    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            uiSettings = uiSettings,
            properties = properties,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            cameraPositionState = cameraPositionState,


            ) {
            // Add a marker at the position of the camera
            //Escuhar los cambiso de camerea position state
            LaunchedEffect(cameraPositionState) {
                markerPosition = cameraPositionState.position.target
            }
            if (!cameraPositionState.isMoving) {
                Marker(
                    //visible = false,
                    state = MarkerState(
                        position = cameraPositionState.position.target,
                    ),
                    icon = getBitmapDescriptor(
                        context = LocalContext.current,
                        id = R.drawable.ic_map_marker
                    )
                )
            }
            if (cameraPositionState.isMoving) {
                Marker(
                    //visible = false,
                    state = MarkerState(
                        position = cameraPositionState.position.target,
                    ),
                    icon = getBitmapDescriptor(
                        context = LocalContext.current,
                        id = R.drawable.ic_map_marker
                    )
                )
            }
            //Log.d("MapScreen", markerPosition.toString())
            //map.addMarker(marker)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (cameraPositionState.isMoving) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_map_marker),
//                    contentDescription = "Marker",
//                    modifier = Modifier
//                        .size(50.dp)
//                        .padding()
//                )

            }
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .width(200.dp)
                        .wrapContentHeight()
                ) {
                    Text(text = "Seleccionar ubicaion")
                }
                Spacer(modifier = Modifier.weight(1f))
//                Text(
//                    text = "Is camera moving: ${cameraPositionState.isMoving}" +
//                            "\n Latitude and Longitude: ${cameraPositionState.position.target.latitude} " +
//                            "and ${cameraPositionState.position.target.longitude}",
//                    textAlign = TextAlign.Center
//                )
                TextField(
                    enabled = false,
                    value = viewModel.destinoAddressText, onValueChange = {
                        viewModel.destinoAddressText
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 56.dp, start = 50.dp, end = 50.dp)
                )
            }
        }
    }

}