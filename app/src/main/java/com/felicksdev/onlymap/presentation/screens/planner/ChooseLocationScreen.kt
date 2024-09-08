
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.Destinations.MapScreen
import com.felicksdev.onlymap.navigation.Destinations.OptimalRoutesScreen
import com.felicksdev.onlymap.presentation.components.LocationOptionItem
import com.felicksdev.onlymap.presentation.components.topBars.TopBar
import com.felicksdev.onlymap.utils.currentRoute
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel


@Preview
@Composable
private fun LocationsSelectionScreenPreview() {
    ChooseLocationsScreen(
        navController = rememberNavController(),
        isOrigin = true,
        plannerViewModel = PlannerViewModel()
    )
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChooseLocationsScreen(
    isOrigin : Boolean,
    navController: NavController,
    plannerViewModel: PlannerViewModel
) {
//    LaunchedEffect(Unit) {
//        plannerViewModel.testSetLocations()
//    }
//    Log.d("ChooseLocationsScreen", "isOrigin: $isOrigin")
    // Inicializar el GeoCoder y obtener la dirección al llegar a la pantalla
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
    }
    Scaffold(
        topBar = {
            TopBar(
                text = "Seleccione "+ if (isOrigin) "origen" else "destino",
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
                Column {
                    Box {
                        Text(
                            text = "Selecciona una ubicación",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    SuggestionList(
                        navController = navController,
                        viewModel = plannerViewModel,
                        isOrigin = isOrigin
                    )
                }

            }
        }
    )
}

@Composable
fun SuggestionList(
    navController: NavController,
    viewModel : PlannerViewModel,
    isOrigin: Boolean
) {
    val route = currentRoute(navController = navController)
     Log.d("ChooseLocationsScreen", "Actual route: $route")
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        LocationOptionItem(
            locationIcon = Icons.Default.MyLocation,
            locationText = "Mi ubicación",
            onLocationSelected = {
                //Logica para  obtener la ubicacion actual
                Log.d("LocationsOptions", "Mi ubicación")
            },

            )
        LocationOptionItem(
            locationIcon = Icons.Default.LocationOn,
            locationText = "Seleccionar ubicación en el mapa",
            onLocationSelected = {
                // Logica para abrir mapa
                try {
                    navController.navigate(MapScreen.route + isOrigin)
                } catch (e: Exception) {
                    Log.d(
                        "LocationOptionItem", "Error al abrir pantalla ${e.message}"
                    )
                }
            },
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                viewModel.testSetLocations()
                Log.d("ChooseLocationsScreen", viewModel.plannerState.value.toPlace.toString())
            }
        ) {
            Text("setViewModel", color = Color.White)

        }
    }
}

@Composable
fun LocationSelector(
    focusRequester: FocusRequester,
    viewModel: LocationViewModel
) {
    //REcuerpar del viewmodel
    val originLocation by viewModel.startLocation.collectAsState()
    val destinationLocation by viewModel.endLocation.collectAsState()

    LocationField(
        locationState = originLocation,
        onFieldSelected = {

        },
        label = "Origen",
    )
    LocationField(
        locationState = destinationLocation,
        onFieldSelected = {
        },
        label = "Destino", focusRequester = focusRequester
    )
}

@Preview
@Composable
private fun LocationSelectionScreenPreview() {
    LocationSelectionScreen()
}
@Composable
fun LocationSelectionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TextField para "Seleccione punto de origen"
        var originPoint by remember { mutableStateOf("") }

        OutlinedTextField(
            value = originPoint,
            onValueChange = { originPoint = it },
            label = { Text("Seleccione punto de origen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Opción "Su ubicación"
        LocationOption(
            icon = Icons.Default.MyLocation, // Usamos un ícono representativo de "ubicación"
            text = "Su ubicación",
            onClick = { /* Acción para "Su ubicación" */ }
        )

        // Opción "Elegir en el mapa"
        LocationOption(
            icon = Icons.Default.Place, // Usamos un ícono representativo del mapa
            text = "Elegir en el mapa",
            onClick = { /* Acción para "Elegir en el mapa" */ }
        )
    }
}

@Composable
fun LocationOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
//                vertical = 20.dp
            )
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}
