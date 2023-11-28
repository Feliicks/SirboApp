package com.felicksdev.onlymap.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SelectLocationScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("SIRBO APP")
                }
            )
        }
    ) {
        Box(){
            Text(text = "Selecciona una ubicación", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.align(Alignment.Center))
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LocationItem(locationIcon = Icons.Default.LocationOn, locationText = "Mi ubicación")
            LocationItem(
                locationIcon = Icons.Default.LocationOn,
                locationText = "Seleccionar ubicación en el mapa"
            )
            LocationItem(locationIcon = Icons.Default.LocationOn, locationText = "Otra ubicación")
        }

    }
}

@Preview
@Composable
fun RouteSelectionDetailDetail() {
    SelectLocationScreen()
}


@Composable
fun LocationItem(locationIcon: ImageVector, locationText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(56.dp)
            .border(1.dp, Color.Black, MaterialTheme.shapes.medium)
            .padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = locationIcon,
            contentDescription = "Location Icon",
            modifier = Modifier
                .padding(start = 25.dp)
        )
        Text(
            text = locationText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp)

        )
        // Agrega aquí cualquier otro elemento que desees en el mismo Row
    }
}

@Preview(showBackground = true)
@Composable
fun LocationItemPreview() {
    LocationItem(locationIcon = Icons.Filled.LocationOn, locationText = "Mi ubicacion")
}

