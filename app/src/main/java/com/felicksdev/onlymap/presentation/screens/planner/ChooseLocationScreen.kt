import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.Destinations.MapScreen
import com.felicksdev.onlymap.navigation.Destinations.OptimalRoutesScreen
import com.felicksdev.onlymap.presentation.components.LocationOptionItem
import com.felicksdev.onlymap.presentation.components.topBars.TopBar
import com.felicksdev.onlymap.viewmodel.PlannerViewModel


@Preview
@Composable
private fun LocationsSelectionScreenPreview() {
    ChooseLocationsScreen(
        navController = rememberNavController(),
        isOrigin = true,
        plannerViewModel = null
    )
}

@Composable
fun ChooseLocationsScreen(
    isOrigin: Boolean,
    navController: NavController,
    onNavigate: (String) -> Unit = {},
    plannerViewModel: PlannerViewModel? = hiltViewModel()
) {
//    val errorState by plannerViewModel.errorState.collectAsState()
    val errorState by plannerViewModel!!.errorState.collectAsState()
    Scaffold(
        topBar = {
            TopBar(
                text = "Seleccione " + if (isOrigin) "origen" else "destino",
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
                        onClick = {
                            plannerViewModel!!.testSetLocations()
                        },
                        onNavigate = {
                            //                      si ambos objeos estan llenos entonces
//                      navegar a la siguiente pantalla de planificaiocn
//                routesViewModel.getOptimalRoutes( originLocation ,destinationLocation)
                            // Todo  hacer el fetch aqui
//                Hacer valdiacion si ambos places estan definidos
                            if (plannerViewModel!!.isPlacesDefined()) {
                                navController.navigate(OptimalRoutesScreen.route)

                                Log.d(
                                    "ChooseLocationsScreen",
                                    "${plannerViewModel!!.isPlacesDefined()}Both places are defined"
                                )
                            } else {
                                Log.d(
                                    "ChooseLocationsScreen",
                                    "${plannerViewModel!!.isPlacesDefined()}Both places are not defined"
                                )
                                // Mostrar dialogo de error
//                    alertstate.true //
//                    alrer.text = "Por favor selecciona un origen y destino"

                            }
                        },
                        navController = navController,
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
    isOrigin: Boolean,
    onClick: () -> Unit = {},
    onNavigate: () -> Unit
) {

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
            onClick = onNavigate,
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
            onClick = onClick
        ) {
            Text("setViewModel", color = Color.White)

        }
    }
}


