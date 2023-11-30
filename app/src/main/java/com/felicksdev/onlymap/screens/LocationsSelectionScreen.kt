import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.felicksdev.onlymap.navigation.Destinations
import com.felicksdev.onlymap.navigation.Destinations.*
import com.felicksdev.onlymap.screens.components.LocationOptionItem

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LocationsSelectionScreen(onNextClick: (String, String) -> Unit, navController: NavController) {
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
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
                OutlinedTextField(
                    value = origin,
                    onValueChange = { origin = it },
                    label = { Text("Origen") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = destination,
                    onValueChange = { destination = it },
                    label = { Text("Destino") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .focusRequester(focusRequester)

                )
                Column {
                    Box() {
                        androidx.compose.material3.Text(
                            text = "Selecciona una ubicación",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()

                        //verticalArrangement = Arrangement.Center,
                        //horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LocationOptionItem(
                            locationIcon = Icons.Default.LocationOn, locationText = "Mi ubicación",
                            navController = navController
                        )
                        LocationOptionItem(
                            locationIcon = Icons.Default.LocationOn,
                            locationText = "Seleccionar ubicación en el mapa",
                            navController = navController
                        )
                        LocationOptionItem(
                            locationIcon = Icons.Default.LocationOn,
                            locationText = "Otra ubicación",
                            navController = navController
                        )
                        Button(
                            onClick = {
                                navController.navigate(MapScreen.route)
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
                }
                
            }
        }
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
