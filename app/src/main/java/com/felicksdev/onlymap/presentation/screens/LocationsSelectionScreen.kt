
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.felicksdev.onlymap.data.models.AddressState
import com.felicksdev.onlymap.navigation.Destinations.LocationsSelectionScreen
import com.felicksdev.onlymap.navigation.Destinations.MapScreen
import com.felicksdev.onlymap.navigation.Destinations.OptimalRoutesScreen
import com.felicksdev.onlymap.presentation.components.LocationOptionItem
import com.felicksdev.onlymap.viewmodel.LocationViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LocationsSelectionScreen(
    locationViewModel: LocationViewModel,
    onNextClick: (String, String) -> Unit,
    navController: NavController,
    routesViewModel: RutasViewModel
) {
    val originLocationState: AddressState = locationViewModel.originLocationState.value!!
    val destinoLocationState: AddressState = locationViewModel.destinationLocationState.value!!
//    settear como origen la ubicación actual
    val focusRequester = remember { FocusRequester() }
    locationViewModel.initializeGeoCoder(context = LocalContext.current)
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    locationViewModel.getLastLocation(context = LocalContext.current);

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Sirbo App", color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LocationsInputs(
                    viewModel = locationViewModel,
                    focusRequester = focusRequester,
                    originAddress = "Ubicación desconocida",
//                    currentLocation = currentLocation,
                    originLocationState = originLocationState,
                    destinationLocationState = destinoLocationState
                )
                Column {
                    Box {
                        Text(
                            text = "Selecciona una ubicación",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    LocationsOptions(
                        navController = navController,
                        locationViewModel = locationViewModel,
                        routesViewModel = routesViewModel,
                        originLocation = originLocationState,
                        destinationLocation = destinoLocationState
                    )
                }

            }
        }
    )
}

@Composable
fun LocationsOptions(
    navController: NavController,
    locationViewModel: LocationViewModel,
    routesViewModel: RutasViewModel,
    originLocation : AddressState,
    destinationLocation : AddressState,

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LocationOptionItem(
            locationIcon = Icons.Default.LocationOn,
            locationText = "Mi ubicación",
            onLocationSelected = {
                locationViewModel.onClickSetMyLocation()
                Log.d("LocationsOptions", "Mi ubicación")
            },

            )
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
            },
        )
        LocationOptionItem(
            locationIcon = Icons.Default.LocationOn,
            locationText = "Otra ubicación",
            onLocationSelected = {}
        )
        Button(
            onClick = {
//                      si ambos objeos estan llenos entonces
//                      navegar a la siguiente pantalla de planificaiocn
//                routesViewModel.getOptimalRoutes( originLocation ,destinationLocation)
                navController.navigate(OptimalRoutesScreen.route)

//                navController.navigate(MapScreen.route)
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
                Text("Siguiente", color = Color.White)
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun LocationsInputs(
    viewModel: LocationViewModel,
    focusRequester: FocusRequester,
    originAddress: String,
//    currentLocation: AddressState,
    originLocationState: AddressState,
    destinationLocationState: AddressState
) {
    LocationField(
        locationState = originLocationState,
        locationAddress = originAddress,
        onFieldSelected = {
            viewModel.onOriginSelected()
            Log.d(
                "LocationField", "El address seleccionado es : ${originLocationState}"
            )
        },
        label = "Origen",
    )
    LocationField(
        locationState = destinationLocationState,
        locationAddress = viewModel.destinoAddressText,
        onFieldSelected = {
            viewModel.onDestinoSelected()
        },
        label = "Destino", focusRequester = focusRequester
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

//@Preview
//@Composable
//fun LocationsSelectionScreenPreview() {
//    LocationsSelectionScreen(
//        locationViewModel = LocationViewModel(),
//        onNextClick = { _, _ -> Unit },
//        navController = NavController(LocalContext.current)
//    )
//}