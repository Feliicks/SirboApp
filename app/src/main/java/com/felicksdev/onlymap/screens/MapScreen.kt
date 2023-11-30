package com.felicksdev.onlymap.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.felicksdev.onlymap.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen() {

    mMap()
    Column {
        Text(text = "Hola soy un Text View")
    }
    Button(onClick = { },
        modifier = Modifier.fillMaxWidth()) {
        Text(text = "Seleccionar ubicaion")


    }
}

@Composable
fun mMap() {
    val markerPosition = LatLng(0.0,0.0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 1f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true
            )
        ) {
            // Add a marker at the position of the camera
            //Escuhar los cambiso de camerea position state
            LaunchedEffect(cameraPositionState) {
                //cameraPositionState.position = CameraPosition.fromLatLngZoom(markerPosition, 1f)
                Log.d("MapScreen", "Marker position: $markerPosition")
            }
            Marker(
                //position = cameraPositionState.center()
                //icon = ImageVector.asset("assets/pin.png")
                state = MarkerState(
                    position = cameraPositionState.position.target,
                    //icon = ImageVector.asset("assets/pin.png")
                )
            )
            //Log.d("MapScreen", markerPosition.toString())
            //map.addMarker(marker)
        }
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            IconButton(onClick = { /*TODO*/ }) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_location),
//                    contentDescription = "marker"
//                )
//            }
//
//            Text(
//                text = "Is camera moving: ${cameraPositionState.isMoving}" +
//                        "\n Latitude and Longitude: ${cameraPositionState.position.target.latitude} " +
//                        "and ${cameraPositionState.position.target.longitude}",
//                textAlign = TextAlign.Center
//            )
//        }
    }

}