    package com.felicksdev.onlymap.presentation.screens

    import RutasViewModel
    import android.util.Log
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.Divider
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Surface
    import androidx.compose.material3.Text
    import androidx.compose.material3.TopAppBar
    import androidx.compose.runtime.Composable


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ThirdScreen(
        viewModel : RutasViewModel
    ) {

        val state = viewModel.state
        val listRutas = viewModel.routesList
        //Log.d("ThirdScreen", "Rutas obtenidas ${state.rutas}")
        Log.d("ThirdScreen", "Rutas obtenidas ${listRutas}")

        Surface(
        ) {
            //color = MaterialTheme.colorScheme.background
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
                    items(listRutas) { ruta ->
                        RouteItem(
                            ruta = ruta,
                            navigateToDetail = {
                                // Llama a la función de navegación del NavController aquí
                                //navController.navigate("fragment_addresses")
                            }
                        )
                        Divider() // Agrega un separador entre elementos, si lo deseas
                    }
                }
            }
        }
    }

