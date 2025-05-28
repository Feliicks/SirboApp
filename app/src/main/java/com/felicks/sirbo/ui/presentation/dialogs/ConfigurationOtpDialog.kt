package com.felicks.sirbo.ui.presentation.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.felicks.sirbo.domain.models.OtpConfig

@Composable
fun OtpConfigDialog(
    initialConfig: OtpConfig,
    onDismiss: () -> Unit,
    onSave: (OtpConfig) -> Unit,
    onReset: () -> Unit
) {
    var mode by remember { mutableStateOf(initialConfig.mode) }
    var walkDistance by remember { mutableStateOf(initialConfig.walkDistance.toFloat()) }
    var maxTransfers by remember { mutableStateOf(initialConfig.maxTransfers) }
    var numItineraries by remember { mutableStateOf(initialConfig.numItineraries) }

    // 🔥 Esta es la clave: re-sincroniza los campos si cambia el config
    LaunchedEffect(initialConfig) {
        mode = initialConfig.mode
        walkDistance = initialConfig.walkDistance.toFloat()
        maxTransfers = initialConfig.maxTransfers
        numItineraries = initialConfig.numItineraries
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Configuración de ruta") },
        text = {
            Column {
                // Modo de transporte
                if (false) {
                    Text("Modo de transporte")
                    DropdownMenuWithOptions(
                        options = listOf("WALK", "TRANSIT,WALK", "BUS", "BICYCLE"),
                        selectedOption = mode,
                        onOptionSelected = { mode = it },
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Distancia caminando
                Text("Distancia caminando: ${walkDistance.toInt()} m")
                Slider(
                    value = walkDistance,
                    onValueChange = { walkDistance = it },
                    valueRange = 0f..5000f,
                    steps = 10
                )

                Spacer(modifier = Modifier.height(12.dp))
//
//                // Transbordos
//                Text("Máx. transbordos")
//                NumberPicker(min = 0, max = 3, value = maxTransfers) {
//                    maxTransfers = it
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // N° de rutas
//                Text("N° de rutas sugeridas")
//                NumberPicker(min = 1, max = 5, value = numItineraries) {
//                    numItineraries = it
//                }
//                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onReset) {
                    Text("Restablecer a valores por defecto")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val updatedConfig = OtpConfig(
                    mode = mode,
                    walkDistance = walkDistance.toInt(),
                    maxTransfers = maxTransfers,
                    numItineraries = numItineraries
                )
                onSave(updatedConfig)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },

        )
}

@Composable
fun DropdownMenuWithOptions(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text("Modo") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}


@Composable
fun NumberPicker(min: Int, max: Int, value: Int, onValueChange: (Int) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { if (value > min) onValueChange(value - 1) }) {
            Icon(Icons.Default.Remove, contentDescription = "Disminuir")
        }
        Text("$value", modifier = Modifier.padding(horizontal = 8.dp))
        IconButton(onClick = { if (value < max) onValueChange(value + 1) }) {
            Icon(Icons.Default.Add, contentDescription = "Aumentar")
        }
    }
}

