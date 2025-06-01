package com.felicks.sirbo.ui.presentation.components.topBars

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicks.sirbo.LocationDetail
import com.felicks.sirbo.ui.navigation.Destinations
import com.felicks.sirbo.viewmodel.PlannerViewModel


@Composable
fun RouterPlannerBar(
    modifier: Modifier = Modifier,
    plannerViewModel: PlannerViewModel,
    onMenuClick: () -> Unit = { },
    navController: NavController
) {

    val toLocation by plannerViewModel.toLocation.collectAsState()

    val fromLocation by plannerViewModel.fromLocation.collectAsState()

    Log.d("RouterPlannerBar", "estado: ${plannerViewModel.isPlacesDefined()}")

    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
            )
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                CustomLocationFormField(
                    navController = navController,
                    modifier = Modifier,
                    isOrigin = true,
                    onSaved = { /* Guardar el valor */ },
                    placeHolder = "Select origin",
                    leadingIcon = Icons.Default.MyLocation,
                    trailing = Icons.Default.Close,
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
                    leadingIcon = Icons.Default.LocationOn,
                    trailing = Icons.Default.SwapVert,
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

@Composable
fun SmallRouterPlannerBar(
    modifier: Modifier = Modifier,
    plannerViewModel: PlannerViewModel = hiltViewModel(),
    onMenuClick: () -> Unit = { },
    navController: NavController
) {

    val toLocation by plannerViewModel.toLocation.collectAsState()

    val fromLocation by plannerViewModel.fromLocation.collectAsState()

    Log.d("SmallRouterPlannerBar", "Estado: ${plannerViewModel.isPlacesDefined()}")

    Column(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.surfaceContainer,
            )
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        // Fila para la barra de menú y los campos de texto
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Campo para seleccionar el origen
                SmallLocationFormField(
                    navController = navController,
                    modifier = Modifier,
                    isOrigin = true,
                    onSaved = { /* Guardar el valor */ },
                    placeHolder = "Seleccione origen",
                    leadingIcon = Icons.Default.MyLocation,
                    trailing = Icons.Default.Close,
                    locationDetail = fromLocation,
                    plannerViewModel = plannerViewModel,
                    onTrailingClick = { plannerViewModel.reset() },
                )
                // Campo para seleccionar el destino
                SmallLocationFormField(
                    navController = navController,
                    isOrigin = false,
                    onSaved = {},
                    placeHolder = "Seleccione destino",
                    leadingIcon = Icons.Default.LocationOn,
                    trailing = Icons.Default.SwapVert,
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


@Composable
fun CustomLocationFormField(
    modifier: Modifier = Modifier,
    isOrigin: Boolean,
    locationDetail: LocationDetail?,
    onSaved: (String) -> Unit,
    placeHolder: String,
    leadingIcon: ImageVector? = null,
    trailing: ImageVector? = null,
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    navController: NavController,
    plannerViewModel: PlannerViewModel,
    onTrailingClick: () -> Unit = {}
) {
    val fromLocation by plannerViewModel.toLocation.collectAsState()
    val toLocation by plannerViewModel.fromLocation.collectAsState()
    val textState = rememberUpdatedState(
        if (isOrigin) fromLocation.description
        else toLocation.description
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icono de Reset (Solo si hay ubicaciones definidas)
        if (plannerViewModel.isPlacesDefined()) {
//        if (plannerViewModel.isAnyPlaceDefined()) {
            trailing?.let {
                IconButton(
                    onClick = onTrailingClick,
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(imageVector = it, contentDescription = "Reset")
                }
            }
        } else {
            Spacer(modifier = Modifier.size(50.dp)) // Ajusta tamaño si es necesario
        }

        Column(modifier = Modifier.weight(1f)) {
            BasicTextField(
                value = textState.value,
                onValueChange = { newValue ->
                    onSaved(locationDetail?.copy(description = newValue).toString())
                },
                modifier = modifier
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    )
                    .border(
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        ), shape = MaterialTheme.shapes.small
                    )
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate(Destinations.ChooseLocations.route + isOrigin)
                        Log.d(
                            "CustomLocationFormField",
                            Destinations.ChooseLocations.route + isOrigin
                        )
                    },
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
                        // Icono Leading (Si existe)
                        leadingIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = "Location Icon",
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }

                        // Campo de texto con Placeholder
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                        ) {
                            if (textState.value.isEmpty()) {
                                Text(
                                    text = placeHolder,
                                    style = LocalTextStyle.current.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                        fontSize = fontSize
                                    )
                                )
                            }
                            innerTextField() // Campo de texto real
                        }
                    }
                }
            )
        }
    }
}


@Composable
fun SmallLocationFormField(
    modifier: Modifier = Modifier,
    isOrigin: Boolean,
    locationDetail: LocationDetail?,
    onSaved: (String) -> Unit,
    placeHolder: String,
    leadingIcon: ImageVector? = null,
    trailing: ImageVector? = null,
    fontSize: TextUnit = MaterialTheme.typography.bodyMedium.fontSize,
    navController: NavController,
    plannerViewModel: PlannerViewModel = hiltViewModel(),
    onTrailingClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TrailingIcon(
            trailing = trailing,
            isPlacesDefined = plannerViewModel.isPlacesDefined(),
//            isPlacesDefined = plannerViewModel.isAnyPlaceDefined(),
            onTrailingClick = onTrailingClick
        )

        LocationTextField(
            modifier = modifier.weight(1f),
            text = locationDetail?.description ?: "",
            placeHolder = placeHolder,
            leadingIcon = leadingIcon,
            fontSize = fontSize,
            onClick = { navController.navigate(Destinations.ChooseLocations.route + isOrigin) }
        )
    }
}

@Composable
fun TrailingIcon(trailing: ImageVector?, isPlacesDefined: Boolean, onTrailingClick: () -> Unit) {
    if (isPlacesDefined) {
        trailing?.let { icon ->
            IconButton(
                onClick = onTrailingClick,
                modifier = Modifier.size(45.dp)
            ) {
                Icon(imageVector = icon, contentDescription = "Reset")
            }
        }
    } else {
        Spacer(modifier = Modifier.size(45.dp)) // Para mantener el diseño consistente
    }
}

@Composable
fun LocationTextField(
    modifier: Modifier = Modifier,
    text: String,
    placeHolder: String,
    leadingIcon: ImageVector? = null,
    fontSize: TextUnit,
    onClick: () -> Unit
) {
    BasicTextField(
        enabled = false,
        value = text,
        onValueChange = {},
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                shape = MaterialTheme.shapes.small
            )
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(4.dp),
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
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono de ubicación (si existe)
                leadingIcon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = "Location Icon",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }

                // Placeholder o contenido del TextField
                Box(modifier = Modifier.weight(1f)) {
                    if (text.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = placeHolder,
                            style = LocalTextStyle.current.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                fontSize = fontSize
                            )
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewSmallLocationFormField() {
    val fakeNavController = rememberNavController()
    SmallLocationFormField(
        isOrigin = true,
        locationDetail = LocationDetail(description = "Avenida Heroes Del Pacífico"),
        onSaved = {},
        placeHolder = "Selecciona una ubicación",
        leadingIcon = Icons.Default.LocationOn,
        trailing = Icons.Default.Clear,
        navController = fakeNavController,
        onTrailingClick = {}
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewLocationFormField() {
    val sampleLocation = LocationDetail(description = "Sample Location")

    MaterialTheme {
        SmallLocationFormField(
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
            leadingIcon = Icons.Default.LocationOn,
            trailing = Icons.Default.Close,

            )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSmallRouterPlannerBar() {

    MaterialTheme {
        SmallRouterPlannerBar(
            onMenuClick = { /* Acción del menú */ },
            navController = rememberNavController()
        )
    }
}



