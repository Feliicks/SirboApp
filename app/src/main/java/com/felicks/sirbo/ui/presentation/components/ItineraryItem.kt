package com.felicks.sirbo.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felicks.sirbo.data.models.TransportMode
import com.felicks.sirbo.data.models.otpModels.routing.FromLeg
import com.felicks.sirbo.data.models.otpModels.routing.Itinerary
import com.felicks.sirbo.data.models.otpModels.routing.Leg
import com.felicks.sirbo.data.models.otpModels.routing.LegGeometry
import com.felicks.sirbo.data.models.otpModels.routing.ToLeg
import com.felicks.sirbo.ui.theme.BusColor
import com.felicks.sirbo.ui.theme.TaxiColor
import com.felicks.sirbo.ui.theme.WalkColor
import com.felicks.sirbo.utils.StringUtils.toTransportMode
import kotlin.math.max

@Composable
fun ItineraryItem(itinerary: Itinerary, onClick: () -> Unit) {
    val maxDuration = itinerary.legs.maxOfOrNull { it.duration } ?: 1.0
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 0.dp, horizontal = 16.dp)
    ) {
        // 📌 Fila superior: Duración y distancia
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${itinerary.getDurationInMinutes()} min",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "(${itinerary.distanceInKm()} km)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 📌 Fila de transportes
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            itinerary.legs.forEach { leg ->
                TransportBadge(leg, maxDuration)
                Spacer(modifier = Modifier.width(6.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                contentDescription = "Ver detalles",
                tint = Color.Gray,
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 📌 Lugar de salida
        Text(
            text = "Ruta desde: ${itinerary.legs.first().from.name}",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 📌 Línea divisoria
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    }
}

@Composable
fun TransportBadge(leg: Leg, maxDuration: Double = 1000.0) {
    val backgroundColor = when (leg.mode.toTransportMode()) {
        TransportMode.WALK -> WalkColor // Gris claro
        TransportMode.BUS -> BusColor// Verde
        TransportMode.TAXI -> TaxiColor // Rojo
        else -> Color.Gray
    }

//    val textColor = if (leg.mode == "WALK") Color.Black else Color.White
//    val textColor = if (leg.mode == "WALK") Color.White else Color.Red
    val textColor = Color.White
    val text = leg.routeShortName ?: ""

    // 📌 Cálculo del ancho proporcional basado en la duración
    val widthFraction = if (maxDuration > 0) (leg.duration / maxDuration).toFloat() else 0.1f

    // 📌 Definir el ancho mínimo basado en el contenido (ícono + texto)
    val minTextWidth = (text.length * 8).dp // Aproximación del ancho del texto
    val minWidth = 40.dp + minTextWidth // 40.dp es el espacio del ícono + padding
    val proportionalWidth = (widthFraction * 200).dp // 200.dp es un ancho de referencia

    val finalWidth = max(minWidth.value, proportionalWidth.value).dp // Se asegura un mínimo visible

    Row(
        modifier = Modifier
            .width(finalWidth)
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Icon(
            imageVector = when (leg.mode) {
                "WALK" -> Icons.Default.DirectionsWalk
                "BUS" -> Icons.Default.DirectionsBus
                else -> Icons.Default.DirectionsWalk
            },
            contentDescription = leg.mode,
            tint = textColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItineraryItemPreview() {
    val sampleItinerary = Itinerary(
        duration = 1200, // 20 minutos en segundos
        elevationGained = 0.0,
        elevationLost = 0.0,
        endTime = System.currentTimeMillis() + 1200000, // +20 minutos
        legs = listOf(
            Leg(
                agencyTimeZoneOffset = 0,
                arrivalDelay = 0,
                departureDelay = 0,
                distance = 500.0,
                duration = 300.0,
                endTime = 600,
                flexDrtAdvanceBookMin = 0.0,
                from = FromLeg(
                    name = "Origen",
                    lat = -68.1508,
                    lon = -16.49561
                ),
                interlineWithPreviousLeg = false,
                legGeometry = LegGeometry(
                    length = 15,
                    points = "pxtcBnu}~KBa@??"
                ),
                routeShortName = "893",
                routeLongName = "MiniBus 893",
                mode = "BUS",
                pathway = false,
                realTime = false,
                rentedBike = false,
                route = "Route 1",
                startTime = System.currentTimeMillis(),
                steps = emptyList(),
                to = ToLeg(
                    name = "Destino", lat = -68.13571, lon = -16.49397
                ),
                transitLeg = true
            ),
            Leg(
                mode = "WALK",
            ), Leg(
                mode = "BUS",
            )
        ),
        startTime = 15651,
        tooSloped = false,
        transfers = 0,
        transitTime = 900,
        waitingTime = 100,
        walkDistance = 300.0,
        walkLimitExceeded = false,
        walkTime = 200
    )


    ItineraryItem(itinerary = sampleItinerary, onClick = {})
}


@Preview(showBackground = true)
@Composable
fun TransportBadgePreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) // Espaciado entre elementos
    ) {
        val maxDuration = 900.0
        TransportBadge(Leg(mode = "WALK", duration = 30.0, routeShortName = null), maxDuration)
        TransportBadge(Leg(mode = "BUS", routeShortName = "27", duration = 360.0), maxDuration)
        TransportBadge(Leg(mode = "TAXI", routeShortName = "116", duration = 500.0), maxDuration)
    }
}

