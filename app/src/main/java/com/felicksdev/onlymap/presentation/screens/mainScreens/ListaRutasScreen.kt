package com.felicksdev.onlymap.presentation.screens.mainScreens

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.felicksdev.onlymap.navigation.Destinations.RouteDetailScreen
import com.felicksdev.onlymap.navigation.plus
import com.felicksdev.onlymap.presentation.screens.RouteItem
import com.felicksdev.onlymap.presentation.screens.SearchBar
import com.felicksdev.onlymap.viewmodel.RoutesViewModel


@Composable
fun RoutesScreenContent(
    bottomPadding: PaddingValues,
    viewModel: RoutesViewModel = hiltViewModel(),
    navController: NavController
) {

    viewModel.obtenerRutas()

    val listRutas by viewModel.routesList.collectAsState()
    Column(modifier = Modifier.padding(bottomPadding)) {
        SearchBar()
        LazyColumn {
            items(listRutas) { ruta ->
                RouteItem(
                    ruta = ruta,
                    navigateToDetail = {
                        viewModel.setRouteSelected(ruta)
                        Log.d(
                            "Routes Screen Ruta seleccina ",
                            "Ruta establecida en el viewmodel sss ${ruta.id} ${ruta.shortName}"
                        )

                        navController.navigate(RouteDetailScreen.route + ruta.id)

                    }
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRutasScreen(
    viewModel: RoutesViewModel = hiltViewModel(),
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
        val fullPading = padding.plus(bottomPadding)
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
    ListaRutasScreen(
        bottomPadding = PaddingValues(0.dp),
        navController = NavController(LocalContext.current)
    )
}

