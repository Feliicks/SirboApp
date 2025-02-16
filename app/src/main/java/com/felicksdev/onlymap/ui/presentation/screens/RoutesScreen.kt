package com.felicksdev.onlymap.ui.presentation.screens

import android.util.Log
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.data.models.rutaTest
import com.felicksdev.onlymap.viewmodel.RoutesViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesScreen(
    viewModel: RoutesViewModel,
    navController: NavController
) {
    val state = viewModel.state
    val rutas = viewModel.allRoutesList
    DisposableEffect(Unit) {
        // Código que se ejecuta cuando la pantalla se carga
        Log.d("RoutesScreen", "La pantalla RoutesScreen se cargó")
        Log.d("RoutesScreen", "Rutas obtenidas ${viewModel.allRoutesList}")

        onDispose { /* Cleanup, si es necesario */ }
    }

    Surface {
        //color = MaterialTheme.colorScheme.background
        Column {
            Log.d("RoutesScreen", "Rutas obtenidas ${viewModel.allRoutesList}")
            TopAppBar(
                title = {
                    Text(text = "Routes")
                }
            )
            // Agrega aquí tu contenido de Compose para el fragmento Routes
            SearchBar(query = "", onQueryChanged = {})
            LazyColumn {

                items(state.rutas) { ruta ->
                    RouteItem(
                        ruta = ruta,
                        navigateToDetail = {
                            // Llama a la función de navegación del NavController aquí
                            //navController.navigate("fragment_addresses")
                        }
                    )
                    HorizontalDivider() // Agrega un separador entre elementos, si lo deseas
                }
            }
        }
    }
}

fun cortarCadena(cadena: String): String {
    var partes = cadena.split("→")

//    println(partes.last())
    return partes.last()
}


fun camelCase(string: String, delimiter: String = " ", separator: String = " "): String {
    return string.split(delimiter).joinToString(separator = separator) {
        it.lowercase().replaceFirstChar { char -> char.titlecase() }
    }
}

fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")
fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
        else it.toString()
    }
}

fun validateString(string: String): String {
    return if (string.isEmpty()) {
        "No disponible"
    } else {
        camelCase(string).trim()
    }
}

@Composable
fun RouteItem(
    ruta: RoutesItem,
    navigateToDetail: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val indication = LocalIndication.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                onClick = navigateToDetail
            ),
        shape = RoundedCornerShape(12.dp), // 🔥 Bordes redondeados
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // 🔥 Elevación sutil
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface, // 🔥 Color adaptable a modo claro/oscuro
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔹 Columna Izquierda (Nombre de Ruta y Tipo)
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ruta.shortName,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = ruta.mode,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 🔹 Columna Central (Destino y Recorrido)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Hacia: ${cortarCadena(ruta.longName)}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = ruta.longName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 🔹 Columna Derecha (Ícono de Mapa)
            Column(
                modifier = Modifier.weight(0.3f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Ver en el mapa",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "Ver en mapa",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun RouteItemPreview() {
    RouteItem(rutaTest, navigateToDetail = {})
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    Log.d("Ñ", "Query: $query")
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
        },
        placeholder = {
            Text(text = "Buscar rutas...")
        },
        singleLine = true
    )
}
