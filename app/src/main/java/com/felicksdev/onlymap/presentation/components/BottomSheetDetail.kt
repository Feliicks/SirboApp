package com.felicksdev.onlymap.presentation.components

import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.felicksdev.onlymap.data.models.otpModels.routing.Leg
import com.felicksdev.onlymap.data.models.sampleLegs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomDetail(
    scaffoldState: BottomSheetScaffoldState,
    legs: List<Leg>
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 16.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                legs.forEach { leg ->

                    Text(

                        text = when (leg.mode){
                            "BUS"-> "Pasa ${leg.routeLongName}"
                            "WALK"-> "Caminar"
                            else -> {
                                "Modo de transporte no identificado"}
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Desde: ${leg.from.name} a ${leg.to.name}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Distancia: ${leg.distance} metros",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Duración: ${leg.duration} seconds",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        content = {

        }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun BottomDetailPreview() {
    val scaffoldState = rememberBottomSheetScaffoldState(
    bottomSheetState = rememberModalBottomSheetState(true)
    )
    BottomDetail(scaffoldState, sampleLegs)
}