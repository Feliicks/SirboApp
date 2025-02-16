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
import com.felicksdev.onlymap.ui.navigation.Destinations.MapScreen
import com.felicksdev.onlymap.ui.navigation.Destinations.OptimalRoutesScreen
import com.felicksdev.onlymap.ui.presentation.components.LocationOptionItem
import com.felicksdev.onlymap.ui.presentation.components.topBars.TopBar
import com.felicksdev.onlymap.viewmodel.PlannerViewModel


@Composable
fun ChooseLocationsScreen(
    isOrigin: Boolean,
    navController: NavController,
    onNavigate: (String) -> Unit = {},
    plannerViewModel: PlannerViewModel = hiltViewModel()
) {
    val errorState by plannerViewModel.errorState.collectAsState()
    val isPlacesDefined = plannerViewModel.isPlacesDefined()

    ChooseLocationsScreenContent(
        isOrigin = isOrigin,
        navController = navController,
        onSetLocation = { plannerViewModel.testSetLocations() },
        onNavigate = {
            if (isPlacesDefined) {
                navController.navigate(OptimalRoutesScreen.route)
                Log.d("ChooseLocationsScreen", "Ambos lugares están definidos")
            } else {
                Log.d("ChooseLocationsScreen", "Faltan lugares por definir")
                // Aquí podrías lanzar un snackbar o diálogo de error
            }
        }
    )
}

@Composable
fun ChooseLocationsScreenContent(
    isOrigin: Boolean,
    navController: NavController,
    onSetLocation: () -> Unit,
    onNavigate: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                text = "Seleccione " + if (isOrigin) "origen" else "destino",
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Text(
                    text = "Selecciona una ubicación",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            SuggestionList(
                onClick = onSetLocation,
                onNavigate = onNavigate,
                navController = navController,
                isOrigin = isOrigin
            )
        }
    }
}


@Composable
fun SuggestionList(
    navController: NavController,
    isOrigin: Boolean,
    onClick: () -> Unit = {},
    onNavigate: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        LocationOptionItem(
            locationIcon = Icons.Default.MyLocation,
            locationText = "Mi ubicación",
            onLocationSelected = { Log.d("LocationsOptions", "Mi ubicación") }
        )

        LocationOptionItem(
            locationIcon = Icons.Default.LocationOn,
            locationText = "Seleccionar ubicación en el mapa",
            onLocationSelected = {
                try {
                    navController.navigate(MapScreen.route + isOrigin)
                } catch (e: Exception) {
                    Log.d("LocationOptionItem", "Error al abrir pantalla ${e.message}")
                }
            }
        )

        Button(
            onClick = onNavigate,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
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

@Preview(showBackground = true)
@Composable
fun PreviewChooseLocationsScreenContent() {
    ChooseLocationsScreenContent(
        isOrigin = true,
        navController = rememberNavController(),
        onSetLocation = { /* Simulación de testSetLocations() */ },
        onNavigate = { /* Simulación de navegación */ }
    )
}


