package com.felicks.sirbo.ui.presentation.screens.mainScreens

import android.util.Log
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.felicks.sirbo.data.models.SyncStatus
import com.felicks.sirbo.data.models.otpModels.routes.RutasItem
import com.felicks.sirbo.extensions.toIndicatorColor
import com.felicks.sirbo.ui.navigation.Destinations.RouteDetailScreen
import com.felicks.sirbo.ui.navigation.plus
import com.felicks.sirbo.ui.presentation.screens.RouteItem
import com.felicks.sirbo.viewmodel.RoutesViewModel
import kotlinx.coroutines.launch


@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 16.dp),
//            .padding(16.dp),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
        },
        placeholder = {
            Text(text = "Busca rutas/lineas de transporte")
        },
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListaRutasScreenContent(
    rutasItems: List<RutasItem>,
    isLoading: Boolean,
    searchQuery: String,
    onRetry: () -> Unit,
    errorMessage: String?,
    onNavigateToDetail: (RutasItem) -> Unit,
    onDismissError: () -> Unit,
    bottomPadding: PaddingValues,
    onSearchQueryChanged: (String) -> Unit,
    isSyncing: Boolean,
    syncStatus: SyncStatus
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val rutasAnimadas = remember { mutableStateListOf<RutasItem>() }
// Actualiza la lista animada cada vez que cambian las rutas
    LaunchedEffect(rutasItems) {
        rutasAnimadas.clear()
        rutasAnimadas.addAll(rutasItems)
    }

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
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.padding(bottom = 80.dp) // ✅ Elevar el Snackbar para evitar la BottomBar
            )
        },
    ) { padding ->
        val fullPadding = padding.plus(bottomPadding)
        Column(modifier = Modifier.padding(bottomPadding)) {
            SearchBar(
                query = searchQuery,
                onQueryChanged = {
                    onSearchQueryChanged(it)
                    Log.d("ListaRutasScreenContent", "Query: $it")
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp)
            )
//                    && syncStatus != SyncStatus.SINCRONIZADO
            if (isSyncing ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = syncStatus.toIndicatorColor()
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.CenterHorizontally
            ) {

                when {
                    isLoading -> {
                        CircularProgressIndicator()
                    }

//                    errorMessage != null -> {
//                        ErrorMessage(message = errorMessage, onRetry = onRetry)
//                    }

                    rutasItems.isEmpty() -> {
                        EmptyStateMessage()
                    }

                    else -> {
                        LazyColumn {
                            items(
                                items = rutasAnimadas,
                                key = { it.id }
                            ) { ruta ->
                                AnimatedRouteItem(
                                    rutasItem = ruta,
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

@Composable
fun AnimatedRouteItem(
    rutasItem: RutasItem,
    modifier: Modifier = Modifier,
    navigateToDetail: () -> Unit
) {
    // Estados para opacidad y desplazamiento
    var alpha2 by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(16f) }
    var offsetX by remember { mutableStateOf(100f) }
    // Lanzar animación al ingresar
    LaunchedEffect(Unit) {
        animate(
            initialValue = 100f,
            targetValue = 0f,
            animationSpec = tween(300)
        ) { value, _ -> offsetX = value }
    }

    RouteItem(
        rutasItem = rutasItem,
        navigateToDetail = navigateToDetail,
        modifier = modifier.graphicsLayer {
            translationX = offsetX
        }
    )
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

    val listaRutas by viewModel.filteredRoutesList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorToastMessage.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()
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
        snackbarHost = {
            SnackbarHost(
                snackbarHostState,
                modifier = Modifier.padding(bottom = 80.dp) // ✅ Elevar el Snackbar para evitar la BottomBar
            )
        },
    ) { padding ->
        val fullPading = padding.plus(bottomPadding)
        ListaRutasScreenContent(
            rutasItems = listaRutas,
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
            isSyncing = isSyncing,
            syncStatus = syncStatus,
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
    val rutasItems = listOf(
        RutasItem(id = "1", shortName = "R1", longName = "Ruta Centro"),
        RutasItem(id = "2", shortName = "R2", longName = "Ruta Norte")
    )

    ListaRutasScreenContent(
        rutasItems = rutasItems,
        errorMessage = null, // 🔥 No hay error en la preview
        onNavigateToDetail = {}, // 🔥 No hace nada en preview
        onDismissError = {}, // 🔥 No hace nada en preview
        bottomPadding = PaddingValues(0.dp),
        isLoading = true,
        onRetry = {},
        onSearchQueryChanged = {},
        searchQuery = "",
        isSyncing = true,
        syncStatus = SyncStatus.SINCRONIZANDO
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


