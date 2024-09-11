
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.Destinations.LocationsSelectionScreen
import com.felicksdev.onlymap.navigation.Destinations.MapScreen
import com.felicksdev.onlymap.navigation.Destinations.OptimalRoutesScreen
import com.felicksdev.onlymap.presentation.components.LocationOptionItem
import com.felicksdev.onlymap.presentation.components.topBars.TopBar
import com.felicksdev.onlymap.viewmodel.LocationViewModel


@Preview
@Composable
private fun LocationsSelectionScreenPreview() {
    LocationsSelectionScreen(
        locationViewModel = LocationViewModel(),
        navController = rememberNavController(),
        routesViewModel = RoutesViewModel()
    )
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LocationsSelectionScreen(
    locationViewModel: LocationViewModel,
    navController: NavController,
    routesViewModel: RoutesViewModel
) {
    // Inicializar el GeoCoder y obtener la dirección al llegar a la pantalla
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopBar(
                text = "Selecciona una ubicación",
                navController = navController
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
    routesViewModel: RoutesViewModel,
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
                // Todo  hacer el fetch aqui
                navController.navigate(OptimalRoutesScreen.route)
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
) {
    val originLocation by viewModel.startLocation.collectAsState()
    val destinationLocation by viewModel.endLocation.collectAsState()

    LocationField(
        locationState = originLocation,
        onFieldSelected = {
            viewModel.onOriginSelected()
            Log.d(
                "LocationField", "El address seleccionado es : ${originLocation}"
            )
        },
        label = "Origen",
    )
    LocationField(
        locationState = destinationLocation,
        onFieldSelected = {
            viewModel.onDestinoSelected()
            Log.d(
                "LocationField", "El address seleccionado es : ${destinationLocation}"
            )
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
