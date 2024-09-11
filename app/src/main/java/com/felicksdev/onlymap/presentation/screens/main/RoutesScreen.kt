package com.felicksdev.onlymap.presentation.screens.main

import RoutesViewModel
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.felicksdev.onlymap.navigation.Destinations.RouteDetailScreen
import com.felicksdev.onlymap.navigation.plus
import com.felicksdev.onlymap.presentation.screens.RouteItem
import com.felicksdev.onlymap.presentation.screens.SearchBar


@Composable
fun RoutesScreenContent(
    bottomPadding: PaddingValues,
    viewModel: RoutesViewModel,
    navController: NavController
) {
    val listRutas by viewModel.routesList.collectAsState()
//    val listRutas = viewModel.routesList
    //Hacer fetch a index Routes aqui
    Log.d("ThirdScreen", "Rutas obtenidas ${listRutas}")
    Column(modifier = Modifier.padding(bottomPadding)) {
        SearchBar()
        LazyColumn {
            items(listRutas) { ruta ->
                RouteItem(
                    ruta = ruta,
                    navigateToDetail = {
                        // Llama a la función de navegación del NavController aquí
                        // Por ejemplo,
                        viewModel.routeSelected = ruta
                        Log.d(
                            "Routes Screen Ruta seleccina ",
                            "Ruta establecida en el viewmodel sss ${viewModel.routeSelected}"
                        )
                        Log.d(
                            "Routes Screen Ruta seleccina ",
                            "Ruta establecida en el viewmodel sss ${ruta}"
                        )
                        if (viewModel.routeSelected != null) {
//                            navController.navigate(RouteDetailScreen.route + "/${ruta.id}")
                            navController.navigate(RouteDetailScreen.route)
//                                navController.navigate(RouteDetailScreen.routeDetailScreenRoute)
                        }
                    }
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesScreen(
    viewModel: RoutesViewModel,
    navController: NavController,
    bottomPadding: PaddingValues
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Busca lineas de transporte")
                }
            )
        }
    ) { padding ->
        val fullPading = padding.plus( bottomPadding)
        RoutesScreenContent(
            bottomPadding = fullPading,
            viewModel = viewModel,
            navController = navController
        )

    }
}

@Preview(showSystemUi = true)
@Composable
fun ThirdScreenPreview(modifier: Modifier = Modifier) {
    RoutesScreen(
        viewModel = RoutesViewModel(),
        bottomPadding = PaddingValues(0.dp),
        navController = NavController(LocalContext.current)
    )
}

