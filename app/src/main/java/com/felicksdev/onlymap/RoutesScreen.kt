package com.felicksdev.onlymap

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.felicksdev.onlymap.data.models.GeometriaRuta
import com.felicksdev.onlymap.data.models.Operador
import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.models.SentidoRuta
import com.felicksdev.onlymap.data.models.TipoRuta
import com.felicksdev.onlymap.data.models.TipoVehiculo
import com.felicksdev.onlymap.ui.theme.OnlyMapTheme
import com.felicksdev.onlymap.viewmodel.RutasViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesScreen(
    viewModel: RutasViewModel,
    navController: NavController
) {
    val state = viewModel.state

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(text = "Routes")
                }
            )
            // Agrega aquí tu contenido de Compose para el fragmento Routes
            SearchBar()
            Text(text = "Hello, this is the Routes screen!")
            LazyColumn {
                items(state.rutas) { ruta ->
                    RouteItem(
                        ruta = ruta,
                        navigateToDetail = {
                            // Llama a la función de navegación del NavController aquí
                            navController.navigate("fragment_addresses")
                        }
                    )
                    Divider() // Agrega un separador entre elementos, si lo deseas
                }
            }
        }
    }
}


@Preview
@Composable
fun RoutesScreenPreview() {
    OnlyMapTheme {
        //RoutesScreen()
        //RouteItemCard(rutaTest)
    }
}
val rutaTest = Ruta(
    id = 1,
    nombre = "893",
    cod_ruta = "m893i",
    recorrido = "Av. Simon Bolivar, Camacho, Av Eliodoro Martinez",
    geometria_ruta = GeometriaRuta(coordinates = listOf(listOf(listOf(0.0, 0.0))), type = "LineString", geoJsonString = "GeoJsonString"),
    tipo_ruta = TipoRuta(id = 1, tipo_ruta = "Metropolitana"),
    sentido_ruta = SentidoRuta(id = 1 , sentido = "i"),
    tipo_vehiculo = TipoVehiculo(id = 1, tipo_vehiculo = "MINIBUS"),
    operador = Operador(id = 1, nombre_sindicato = "Sindicato SIMON BOLIVAR"),
    ruta_anterior = "387",
)

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
fun RouteItem(ruta: Ruta, navigateToDetail: (Ruta) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
            .clickable {
                navigateToDetail(ruta)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna izquierda
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .border(1.dp, Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Text(text = ruta.nombre, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                //Text(text = "Línea ${ruta.id}", color = Color.Gray, fontSize = 14.sp)
                //Text(text = validateString(ruta.tipo_vehiculo.tipo_vehiculo), color = Color.Gray, fontSize = 14.sp)
                Text(text = ruta.tipo_vehiculo?.tipo_vehiculo ?: "No disponible", color = Color.Gray, fontSize = 14.sp)

            }

            // Columna central
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
            ) {
                Text(text = "Ruta: ${ruta.nombre}")
                ruta.operador?.let {
                    Text(text = validateString(it.nombre_sindicato))
                }
                val recorridoArr = ruta.recorrido.split(", ")
                Text(text = "${validateString(recorridoArr[0])} - ${validateString(recorridoArr[recorridoArr.size - 1])}")
                //Text(text = "Recorrido: ${ruta.recorrido}")
                Spacer(modifier = Modifier.height(8.dp))
            }
            // Columna derecha
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .border(1.dp, Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                // Ícono de tipo de ruta
                Column(

                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null, // TODO: Agrega una descripción adecuada
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp),
                    )
                    Text(
                        text = "Ver en el mapa",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }


            }

        }
    }
}


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "NUMERO DE RUTA",
        onValueChange = { },
        label = { Text("NUMERO DE RUTA") }
    )
}