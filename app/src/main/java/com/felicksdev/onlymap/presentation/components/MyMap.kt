package com.felicksdev.onlymap.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.felicksdev.onlymap.utils.MapConfig
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings

@Composable
fun MyMap(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    mapConfiguration: MapProperties = MapConfig.mapProperties,
    mapUiConfiguration: MapUiSettings = MapConfig.mapUiConfig,
    padding: PaddingValues = PaddingValues(0.dp),
    markers: @Composable () -> Unit = {}
) {
    GoogleMap(
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiConfiguration,
        properties = mapConfiguration,
    ) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            IconButton(
//                onClick = {
//                },
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_map_marker),
//                    contentDescription = "marker",
//                )
//            }
//            Text(text = "Is camera moving: ${cameraPositionState.isMoving}" +
//                    "\n Latitude and Longitude: ${cameraPositionState.position.target.latitude} " +
//                    "and ${cameraPositionState.position.target.longitude}",
//                textAlign = TextAlign.Center
//            )
//        }
        markers()
    }
}

@Preview
@Composable
private fun MuMapPreview() {
    MyMap(cameraPositionState = MapConfig.initialState)
}