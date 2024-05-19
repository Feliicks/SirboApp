import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.felicksdev.onlymap.data.models.LocationInfo
import com.felicksdev.onlymap.navigation.Destinations.*
import com.felicksdev.onlymap.presentation.screens.components.LocationOptionItem
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun LocationField2(locationAddress: String, onFieldSelected: () -> Unit) {
    OutlinedTextField(
        value = locationAddress,
        onValueChange = { },
        label = { Text("Origen") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onFieldSelected()
                        }
                    }
                }
            },
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LocationsSelectionScreen(
    locationViewModel: LocationViewModel,
    onNextClick: (String, String) -> Unit, navController: NavController
) {
//    val originFieldIsSelected: Boolean by locationViewModel.origenFieldSelected.observeAsState()
//    val destinoFieldSelected: Boolean  = locationViewModel.origenFieldSelected.value ?: false

    val focusRequester = FocusRequester()
    locationViewModel.initializeGeoCoder(context = LocalContext.current)
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    locationViewModel.getLastLocation(context = LocalContext.current);
    //locationViewModel.getAddressOrigen(locationViewModel.origenCoordinates)
    val currentAddress: String by locationViewModel.originAddressText.observeAsState("")
    val origenLocation: LocationInfo by locationViewModel.originLocation.observeAsState(
        LocationInfo(
            LatLng(0.0, 0.0),
            ""
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("SIRBO APP")
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LocationField(
                    currentAddress,
                    { locationViewModel.onOrigenSelected() },
                    label = "Origen"
                )
                LocationField(
                    locationAddress = locationViewModel.destinoAddressText,
                    onFieldSelected = { locationViewModel.onDestinoSelected() },
                    label = "Destino", focusRequester = focusRequester
                )


                Column {
                    Box() {
                        Text(
                            text = "Selecciona una ubicación",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()

                        //verticalArrangement = Arrangement.Center,
                        //horizontalAlignment = Alignment.CenterHorizontally]
                    ) {
                        LocationOptionItem(
                            locationIcon = Icons.Default.LocationOn, locationText = "Mi ubicación",
                            onLocationSelected = {}
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LocationOptionItem(
                            locationIcon = Icons.Default.LocationOn,
                            locationText = "Seleccionar ubicación en el mapa",
                            onLocationSelected = {
                                try {
                                    navController.navigate(MapScreen.route)
                                } catch (e: Exception) {
                                    Log.d(
                                        "LocationOptionItem", "Error al abrir pantalla ${e.message}"
                                    )
                                }
                            }
                        )
                        LocationOptionItem(
                            locationIcon = Icons.Default.LocationOn,
                            locationText = "Otra ubicación",
                            onLocationSelected = {}
                        )
                        Button(
                            onClick = {
                                navController.navigate(MapScreen.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            enabled = true
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Siguiente")
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }

            }
        }
    )
}

@Composable
fun DestinoField2(
    destinoAddressText: String,
    focusRequester: FocusRequester,
    onFieldSelected: () -> Unit
) {
    OutlinedTextField(
        value = destinoAddressText,
        onValueChange = { destinoAddressText },
        label = { Text("Destino") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .focusRequester(focusRequester),
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onFieldSelected()
                        }
                    }
                }
            },

        )
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview(navController: NavController? = null) {
    Button(
        onClick = {
            navController!!.navigate(LocationsSelectionScreen.route)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Siguiente")
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectRouteScreenPreview() {
    //LocationsSelectionScreen(onNextClick = { _, _ -> }, navController = NavController(LocalContext.current)
}
