package com.felicksdev.onlymap.ui.presentation.components.bottomBars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng

@Composable
fun BottomSearchBar(
    isOrigin: Boolean,
    modifier: Modifier = Modifier,
    address: String = "Avenida Ayacucho",
    latLng: LatLng,
    onConfirm: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp), // Padding for better spacing
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start // Align children to the start (left)
    ) {
        Text(
            text = address,
            modifier = Modifier
                .fillMaxWidth() // Fill width so that the alignment to the left is effective
                .padding(bottom = 10.dp) // Space between the text and button
        )
        Text(
            text = "New Text", // New text added
            modifier = Modifier
                .fillMaxWidth() // Space to the left
                .padding(bottom = 10.dp) // Space between the text and button
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(), // Fill width for proper alignment
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxWidth(),// Space to the right
                textAlign = TextAlign.End,
                text = latLng.toString(),
            )
        }

        // Using Box to center the button horizontally
        Box(
            modifier = Modifier
                .fillMaxWidth() // Fill width to center the button horizontally
                .padding(top = 16.dp) // Space between the texts and button
        ) {
            Button(
                onClick = { onConfirm() },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = "Confirmar ubicación")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBottomSearchBar() {
    BottomSearchBar(
        isOrigin = true,
        address = "Avenida Ayacucho",
        latLng = LatLng(0.0, 0.0),
        onConfirm = { /* Handle the confirm action */ }
    )
}