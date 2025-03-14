package com.felicksdev.onlymap.ui.presentation.components.bottomBars

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.felicksdev.onlymap.data.models.TransportMode
import com.felicksdev.onlymap.data.models.getColor
import com.felicksdev.onlymap.data.models.otpModels.routing.FromLeg
import com.felicksdev.onlymap.data.models.otpModels.routing.Itinerary
import com.felicksdev.onlymap.data.models.otpModels.routing.Leg
import com.felicksdev.onlymap.data.models.otpModels.routing.sampleItineraries
import com.felicksdev.onlymap.formatDistance
import com.felicksdev.onlymap.formatDuration
import com.felicksdev.onlymap.ui.presentation.components.ItineraryItem
import com.felicksdev.onlymap.utils.StringUtils.toTransportMode
import com.google.gson.Gson
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDetail(
    scaffoldState: BottomSheetScaffoldState,
    itineraries: List<Itinerary>
) {

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 16.dp,
        sheetContent = {
            SheetContent(listItineraries = itineraries)
        },
    ) {}
}


@Composable
fun ItineraryDetail(itinerary: Itinerary, onBackPressed: () -> Unit = {}) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 0.dp)
    ) {
        // 📌 Botón de Volver + Duración
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(0.10f)
                    .align(Alignment.CenterVertically),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "Volver",
                        tint = Color.Gray
                    )
                }
            }
            Row(
                modifier = Modifier
                    .weight(0.95f), // 95% del ancho
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${itinerary.getDurationInMinutes()} min",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f)) // Empuja el siguiente texto a la derecha
                Text(
                    text = "${formatDistance(itinerary.walkDistance)} - Caminando",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 📌 Lista de Tramos del Itinerario
        Column {
            itinerary.legs.forEachIndexed { index, leg ->
                LegItem(leg, isLast = index == itinerary.legs.lastIndex)
            }
        }
    }
}

@Composable
fun LegItem(leg: Leg, isLast: Boolean) {
    val transportMode = TransportMode.fromMode(leg.mode) // Obtener color e ícono
    val iconSize = 20.dp // Tamaño del ícono dentro del fondo circular
    val circleSize = 28.dp // Tamaño del fondo del ícono
    val lineThickness = 3.dp // Grosor de la línea de conexión
    val lineColor = transportMode.getColor() // Color de la línea y el ícono

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 📌 Ícono con fondo circular
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .background(color = lineColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = transportMode.icon,
                    contentDescription = leg.mode,
                    modifier = Modifier.size(iconSize),
                    tint = Color.White // Ícono blanco dentro del círculo
                )
            }

            // 📌 Línea de conexión entre Legs
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(lineThickness)
                        .height(35.dp) // Espacio entre los pasos
                        .background(lineColor)
                )
            }
        }

        // 📌 Información del `Leg`
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = leg.from.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = when (leg.mode.toTransportMode()) {
                    TransportMode.BUS -> "Ruta ${leg.routeShortName ?: "Desconocida"} • ${
                        formatDuration(
                            leg.duration
                        )
                    } • ${formatDistance(leg.distance)}"

                    TransportMode.WALK
                        -> "Caminar ${formatDuration(leg.duration)} (${formatDistance(leg.distance)})"

                    else -> "${leg.mode}: ${formatDuration(leg.duration)}"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
fun SheetContent(
    listItineraries: List<Itinerary>? = null,
    onItinerarySelectedIndex: (Itinerary) -> Unit = {}
) {
    var selectedItinerary by remember { mutableStateOf<Itinerary?>(null) }
    val pagerState = rememberPagerState { listItineraries?.size ?: 0 }
    val coroutineScope = rememberCoroutineScope()
    Log.d("SheetContent", "itineraries: ${Gson().toJson(listItineraries)}")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 0.dp)
    ) {
        listItineraries?.let { it ->
            HorizontalPager(
                state = pagerState, userScrollEnabled = false,
                modifier = Modifier.padding(0.dp)
            ) { page ->
                when (page) {
                    0 -> { // Página 1: Lista de itinerarios
                        ItineraryList(listItineraries) { itinerary ->
                            selectedItinerary = itinerary
                            onItinerarySelectedIndex(itinerary)
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1) // Desliza a la página de detalles
                            }
                        }
                    }

                    1 -> { // Página 2: Lista de legs (detalles del itinerario)
                        selectedItinerary?.let {
                            ItineraryLegsScreen(it, onBackPressed = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(0) // Desliza a la página de lista de itinerarios
                                }
                            })
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ItineraryLegsScreen(itinerary: Itinerary, onBackPressed: () -> Unit) {
    ItineraryDetail(itinerary, onBackPressed)
}

@Composable
fun ItineraryList(
    itineraries: List<Itinerary>,
    onItinerarySelected: (Itinerary) -> Unit
) {
    LazyColumn {
        items(itineraries) { itinerary ->
            ItineraryItem(itinerary = itinerary, onClick = { onItinerarySelected(itinerary) })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SheetContentPreview() {
    SheetContent(sampleItineraries)
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun ItineraryDetailPreview() {
    MaterialTheme {
        ItineraryDetail(
            itinerary = Itinerary(
                duration = 1800,
                startTime = 5000,
                endTime = 500,
                walkDistance = 1500.0,
                transfers = 2,
//                startLocation = "Plaza Central",
                legs = listOf(
                    Leg(mode = "WALK", distance = 300.0, duration = 5.0),
                    Leg(mode = "BUS", routeShortName = "116", distance = 4000.0, duration = 15.0),
                    Leg(mode = "WALK", distance = 200.0, duration = 3.0),
                    Leg(mode = "BUS", routeShortName = "45", distance = 5000.0, duration = 20.0),
                    Leg(mode = "WALK", distance = 100.0, duration = 2.0)
                )
            ),
            onBackPressed = {} // Simulación de botón atrás
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLegItem() {
    val sampleBusLeg = Leg(
        mode = "BUS",
        routeShortName = "120",
        duration = 15.0,
        distance = 3.5,
        from = FromLeg(name = "Parada Central")
    )

    val sampleWalkLeg = Leg(
        mode = "WALK",
        duration = 5.0,
        distance = 500.0,
        from = FromLeg(name = "Calle 8 de Julio")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LegItem(sampleBusLeg, isLast = false)
        Spacer(modifier = Modifier.height(12.dp))
        LegItem(sampleWalkLeg, isLast = true)
    }
}

