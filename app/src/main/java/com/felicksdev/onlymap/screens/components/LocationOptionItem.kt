package com.felicksdev.onlymap.screens.components

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.felicksdev.onlymap.navigation.Destinations.MapScreen

@Composable
fun LocationOptionItem(
    locationIcon: ImageVector,
    locationText: String,
    //navController: NavController,
    onLocationSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .border(1.dp, Color.Black, MaterialTheme.shapes.medium)
            .padding(bottom = 4.dp)
            .clickable {
                Log.d("LocationOptionItem", "Abriendo pantalla")
                onLocationSelected()
//                try {
//                    navController.navigate(MapScreen.route)
//                } catch (e: Exception) {
//                    Log.d("LocationOptionItem", "Error al abrir pantalla ${e.message}")
//                }
            },
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

@Preview
@Composable
fun LocationOptionPreview() {
    //LocationOptionItem(locationIcon = Icons.Default.LocationOn, locationText = "Texto de prueba")
}