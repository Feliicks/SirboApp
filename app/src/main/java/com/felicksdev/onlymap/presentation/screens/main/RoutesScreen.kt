package com.felicksdev.onlymap.presentation.screens.main

import RutasViewModel
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.felicksdev.onlymap.navigation.Destinations.RouteDetailScreen
import com.felicksdev.onlymap.presentation.screens.RouteItem
import com.felicksdev.onlymap.presentation.screens.SearchBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThirdScreen(
    viewModel: RutasViewModel,
    defaultPadding: PaddingValues,
    navController: NavController
) {

    val listRutas = viewModel.routesList
    Log.d("ThirdScreen", "Rutas obtenidas ${listRutas}")

    Surface(
        modifier = Modifier.padding(defaultPadding)
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(text = "Explorar lineas de transporte")
                }
            )
            SearchBar()
            Text(text = "Hello, this is the Routes screen!")
            LazyColumn {
                items(listRutas) { ruta ->
                    RouteItem(
                        ruta = ruta,
                        navigateToDetail = {
                            // Llama a la función de navegación del NavController aquí
                            // Por ejemplo,
                            viewModel.routeSelected = ruta
                            Log.d("Routes Screen Ruta seleccina ", "Ruta establecida en el viewmodel sss ${viewModel.routeSelected}")
                            if (viewModel.routeSelected != null) {
                                navController.navigate(RouteDetailScreen.route)
//                                navController.navigate(RouteDetailScreen.routeDetailScreenRoute)
                            }
                        }
                    )
                    Divider() // Agrega un separador entre elementos, si lo deseas
                }
            }
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun ThirdScreenPreview(modifier: Modifier = Modifier) {
//    ThirdScreen(
//        viewModel = RutasViewModel(),
//        defaultPadding = PaddingValues(0.dp),
//        navController = NavController(LocalContext.current)
//    )
//}

