package com.felicksdev.onlymap.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.TrufiLocation
import com.felicksdev.onlymap.navigation.Destinations
import com.felicksdev.onlymap.viewmodel.PlannerViewModel


@Composable
fun RouterPlannerBar(
    modifier: Modifier = Modifier,
    plannerViewModel: PlannerViewModel,
    onMenuClick: () -> Unit = { },
    navController: NavController
) {
    val plannerState by plannerViewModel.plannerState.collectAsState()

    val toLocation = plannerState.toPlace

    val fromLocation = plannerState.fromPlace


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fila para la barra de menú y los campos de texto
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Campo para seleccionar el origen
                CustomLocationFormField(
                    navController = navController,
                    modifier = Modifier,
                    isOrigin = true,
                    onSaved = { /* Guardar el valor */ },
                    placeHolder = "Select origin",
                    textLeadingIcon = {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Origin"
                        )
                    },
                    trailing =  Icons.Default.Close,
                    locationDetail = fromLocation,
                    plannerViewModel = plannerViewModel,
                    onTrailingClick = { plannerViewModel.reset() },
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Campo para seleccionar el destino
                CustomLocationFormField(
                    navController = navController,
                    isOrigin = false,
                    onSaved = {},
                    placeHolder = "Select destination",
                    textLeadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Destination"
                        )
                    },
                    trailing =  Icons.Default.SwapVert,
                    onTrailingClick = { plannerViewModel.swapLocations() },
                    locationDetail = toLocation,
                    plannerViewModel = plannerViewModel
                )
            }
            // Botón del menú (hamburguesa)
            IconButton(
                onClick = { onMenuClick() },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Menu"
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationFormField(
    isOrigin: Boolean,
    locationDetail: TrufiLocation?,
    onSaved: (String) -> Unit,
    hintText: String,
    textLeadingIcon: @Composable () -> Unit,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono o contenido del trailing
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            trailing?.invoke()
        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            // OutlinedTextField
            OutlinedTextField(
                value = locationDetail?.description ?: "",
                onValueChange = { newValue ->
                    onSaved(locationDetail?.copy(description = newValue).toString())
                },
                label = { Text(hintText) },
                leadingIcon = textLeadingIcon,

                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }


    }
}


@Composable
fun CustomLocationFormField(
    modifier: Modifier = Modifier,
    isOrigin: Boolean,
    locationDetail: TrufiLocation?,
    onSaved: (String) -> Unit,
    placeHolder: String,
    textLeadingIcon: @Composable () -> Unit,
    trailing: ImageVector? = null,
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    navController: NavController,
    plannerViewModel: PlannerViewModel,
    onTrailingClick : () -> Unit = {}
) {
//    var text by rememberSaveable { mutableStateOf(locationDetail?.description ?: "") }
    val locationInfo by plannerViewModel.plannerState.collectAsState()
    var locationSelected = if (isOrigin) locationInfo.fromPlace
        ?: TrufiLocation() else locationInfo.toPlace?: TrufiLocation()
    var text = if (isOrigin) locationInfo.fromPlace?.description
        ?: "" else locationInfo.toPlace?.description ?: ""
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (plannerViewModel.isPlacesDefined()){
                trailing?.let {icon ->
                    IconButton(
                        onClick = { onTrailingClick() },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "Reset"
                        )
                    }
                }
            }
            else Spacer(modifier = Modifier.size(50.dp)) // Adjust size as needed


        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            BasicTextField(
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    navController.navigate(Destinations.ChooseLocations.route + isOrigin)
                                    Log.d(
                                        "CustomLocationFormField",
                                        Destinations.ChooseLocations.route + isOrigin
                                    )
                                }
                            }
                        }
                    },
                value = text,
                onValueChange = { newValue ->
                    text = newValue
                    onSaved(locationDetail?.copy(description = newValue).toString())
                },
                modifier = modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    )
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                        shape = MaterialTheme.shapes.small
                    )
                    .fillMaxWidth()
                    .padding(8.dp),
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                textStyle = LocalTextStyle.current.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = fontSize
                ),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Leading icon (if provided)
                        if (textLeadingIcon != null) {
                            textLeadingIcon()
                        }

                        // Text field content
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = placeHolder,
                                    style = LocalTextStyle.current.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                        fontSize = fontSize
                                    )
                                )
                            }
                            innerTextField() // The actual text field content
                        }
                    }
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewLocationFormField() {
    val sampleLocation = TrufiLocation(description = "Sample Location")

    MaterialTheme {
        CustomLocationFormField(
            navController = rememberNavController(),
            isOrigin = true,
            locationDetail = sampleLocation,
            onSaved = { /* Guardar el valor */ },
            placeHolder = "Select location",
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(percent = 50)
                )
                .padding(4.dp)
                .height(60.dp),
            textLeadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Icon"
                )
            },
            trailing = Icons.Default.Close,
            plannerViewModel = PlannerViewModel()

        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRouterPlannerBar() {
    val previewViewModel = PlannerViewModel()

    MaterialTheme {
        RouterPlannerBar(
            plannerViewModel = previewViewModel,
            onMenuClick = { /* Acción del menú */ },
            navController = rememberNavController()
        )
    }
}



