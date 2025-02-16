package com.felicksdev.onlymap.ui.presentation.screens.mainScreens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.felicksdev.onlymap.data.models.otpModels.routes.RoutesItem
import com.felicksdev.onlymap.ui.navigation.Destinations.RouteDetailScreen
import com.felicksdev.onlymap.ui.navigation.plus
import com.felicksdev.onlymap.ui.presentation.screens.RouteItem
import com.felicksdev.onlymap.ui.presentation.screens.SearchBar
import com.felicksdev.onlymap.viewmodel.RoutesViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRutasScreenContent(
    rutas: List<RoutesItem>,
    isLoading: Boolean,
    searchQuery: String,
    onRetry: () -> Unit,
    errorMessage: String?,
    onNavigateToDetail: (RoutesItem) -> Unit,
    onDismissError: () -> Unit,
    bottomPadding: PaddingValues,
    onSearchQueryChanged: (String) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Mostrar el Snackbar cuando hay un error
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Busca líneas de transporte") }
            )
        },
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.padding(bottom = 80.dp) // ✅ Elevar el Snackbar para evitar la BottomBar
            )
        },
    ) { padding ->
        val fullPadding = padding.plus(bottomPadding)
        Column(modifier = Modifier.padding(bottomPadding)) {
            SearchBar(query = searchQuery, onQueryChanged = {
                onSearchQueryChanged(it)
                Log.d("ListaRutasScreenContent", "Query: $it")
            })
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                when {
                    isLoading -> {
                        CircularProgressIndicator()
                    }

                    errorMessage != null -> {
                        ErrorMessage(message = errorMessage, onRetry = onRetry)

                    }

                    rutas.isEmpty() -> {
                        EmptyStateMessage()
                    }

                    else -> {
                        LazyColumn(modifier = Modifier.padding()) {
                            items(rutas) { ruta ->
                                RouteItem(
                                    ruta = ruta,
                                    navigateToDetail = { onNavigateToDetail(ruta) }
                                )
                            }
                        }
                    }
                }
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
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    val rutas by viewModel.filteredRoutesList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()

    Log.d("ListaRutasScreen", "estado de carga$isLoading")
    // Mostrar el Snackbar cuando hay un error
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            Log.d("ListaRutasScreen", "Error message: $errorMessage")
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.obtenerRutas()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Busca lineas de transporte")
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.padding(bottom = 80.dp) // ✅ Elevar el Snackbar para evitar la BottomBar
            )
        },
    ) { padding ->
        val fullPading = padding.plus(bottomPadding)
        ListaRutasScreenContent(
            rutas = rutas,
            errorMessage = errorMessage,
            onNavigateToDetail = { ruta ->
                viewModel.setRouteSelected(ruta)
                navController.navigate(RouteDetailScreen.route + ruta.id)
            },
            onDismissError = { viewModel.clearError() },
            bottomPadding = fullPading,
            isLoading = isLoading,
            onRetry = { viewModel.obtenerRutas() },
            searchQuery = searchQuery,
            onSearchQueryChanged = { viewModel.setSearchQuery(it) },
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


@Preview(showSystemUi = true)
@Composable
fun PreviewListaRutasScreen(
) {
    val rutas = listOf(
        RoutesItem(id = "1", shortName = "R1", longName = "Ruta Centro"),
        RoutesItem(id = "2", shortName = "R2", longName = "Ruta Norte")
    )

    ListaRutasScreenContent(
        rutas = rutas,
        errorMessage = null, // 🔥 No hay error en la preview
        onNavigateToDetail = {}, // 🔥 No hace nada en preview
        onDismissError = {}, // 🔥 No hace nada en preview
        bottomPadding = PaddingValues(0.dp),
        isLoading = true,
        onRetry = {},
        onSearchQueryChanged = {},
        searchQuery = ""
    )
}


@Composable
fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = "Reintentar")
        }
    }
}


@Composable
fun EmptyStateMessage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Sin resultados",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "No hay rutas disponibles.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}


