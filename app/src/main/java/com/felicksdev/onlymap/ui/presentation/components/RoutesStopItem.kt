package com.felicksdev.onlymap.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RouteStopItem(
    stopName: String,
    isFirst: Boolean = false,
    isLast: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🔹 Indicador Visual (Línea + Círculo)
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(50.dp), // Ajustar según necesidad
            verticalArrangement = Arrangement.Center
        ) {
            if (!isFirst) {
                Divider(
                    modifier = Modifier
                        .width(2.dp)
                        .height(12.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        if (isFirst) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
            )

            if (!isLast) {
                Divider(
                    modifier = Modifier
                        .width(2.dp)
                        .height(12.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // 🔹 Nombre de la Parada
        Text(
            text = stopName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRouteStopItem() {
    MaterialTheme {
        Column {
            RouteStopItem(stopName = "Río Taquiña", isFirst = true)
            RouteStopItem(stopName = "Calle General Bilbao Rioja")
            RouteStopItem(stopName = "Avenida Simón López", isLast = true)
        }
    }
}