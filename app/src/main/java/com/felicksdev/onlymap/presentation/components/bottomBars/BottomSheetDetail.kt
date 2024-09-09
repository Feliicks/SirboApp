package com.felicksdev.onlymap.presentation.components.bottomBars

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.felicksdev.onlymap.data.models.otpModels.routing.Itinerary
import com.felicksdev.onlymap.data.models.otpModels.routing.Leg
import com.felicksdev.onlymap.data.models.otpModels.routing.sampleItineraries


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
            SheetContent(itineraries = itineraries)
        },
    ){}
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomItineraryBar(
    itineraries: List<Itinerary>
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberModalBottomSheetState(false)
    )
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 16.dp,
        sheetContent = {
            SheetContent(itineraries = itineraries)
        },
        content = {

        }
    )
}

@Composable
fun ItineraryDetail(legs: List<Leg>) {
    Row(modifier = Modifier.border(2.dp, Color.Black, MaterialTheme.shapes.medium)) {
        legs.forEach { leg ->
            Text(
                modifier = Modifier.padding(8.dp),
                text = when (leg.mode) {
                    "BUS" -> "Pasa ${leg.routeLongName}"
                    "WALK" -> "Caminar"
                    else -> {
                        "Modo de transporte no identificado"
                    }
                },
                style = MaterialTheme.typography.bodyMedium
            )
//            Text(
//                text = "Desde: ${leg.from.name} a ${leg.to.name}",
//                style = MaterialTheme.typography.bodySmall
//            )
//            Text(
//                text = "Distancia: ${leg.distance} metros",
//                style = MaterialTheme.typography.bodySmall
//            )
//            Text(
//                text = "Duración: ${leg.duration} seconds",
//                style = MaterialTheme.typography.bodySmall
//            )
        }
    }
}

@Composable
fun ItineraryInfo(itinerary: Itinerary) {
    Row {
        Text(text = "Total time: " + itinerary.getDurationInMinutes() + " minutes")
        Text(text = "(10km)")
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Salida: ${itinerary.startTime} - Llegada: ${itinerary.endTime}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomItineraryPreview() {
    BottomItineraryBar(sampleItineraries)
}


@Composable
private fun SheetContent(
    itineraries: List<Itinerary>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        itineraries.forEach { itinerary ->
            ItineraryInfo(itinerary = itinerary)
            ItineraryDetail(legs = itinerary.legs)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SheetContentPreview() {
    SheetContent(sampleItineraries)
}
