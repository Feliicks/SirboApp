package com.felicks.sirbo.ui.presentation.screens.planner

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.felicks.sirbo.LocationDetail
import com.felicks.sirbo.data.models.RutaGuardadaDomain
import com.felicks.sirbo.data.remote.photon.PhotonFeature
import com.felicks.sirbo.data.remote.photon.toCompactLabel
import com.felicks.sirbo.ui.navigation.Destinations.MapScreen
import com.felicks.sirbo.ui.presentation.components.topBars.SearchTopBar
import com.felicks.sirbo.viewmodel.LocationViewModel
import com.felicks.sirbo.viewmodel.PlannerViewModel

@Composable
fun AlternativeChooseLocationScreen(
    isOrigin: Boolean,
    navController: NavController,
    viewModel: LocationViewModel = hiltViewModel(),
    plannerViewModel: PlannerViewModel = hiltViewModel()
) {
    viewModel.getRutasRecientes()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val rutasRecientes by viewModel.recentPlaces.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    ChooseLocationScreenContent(
        isOrigin = isOrigin,
        searchResults = searchResults,
        recentPlaces = rutasRecientes,
        onLocationSelected = { location ->
            navController.popBackStack()
        },
        onNavigateToMap = {
            try {
                navController.navigate(MapScreen.route + isOrigin)
            } catch (e: Exception) {
                Log.d("LocationOptionItem", "Error al abrir pantalla ${e.message}")
            }
        },
        searchQuery = searchQuery,
        onSearchQueryChanged = viewModel::updateSearchQuery,
        onDebugLocationClick = {
            plannerViewModel.testSetLocations()
        },
        onBackPressed = {
            navController.popBackStack()
        },
        onMyLocation = {
            Log.d("LocationsOptions", "Mi ubicación")
            plannerViewModel.obtenerUbicacion(isOrigin)
            navController.popBackStack()
        },
        onRecentLocationClick = { it ->
            Log.d("LocationsOptions", "Click en Ubicacion reciente")
//            plannerViewModel.obtenerUbicacionReciente(isOrigin)
            val origen = LocationDetail(
                description = it.direccionOrigen,
                address = "",
                latitude = it.origenLat,
                longitude = it.origenLon
            )
            val destino = LocationDetail(
                description = it.direccionDestino,
                address = "",
                latitude = it.destinoLat,
                longitude = it.destinoLon
            )
            Log.d("LocationsOptions", "Detalle $origen   AND $destino")

            plannerViewModel.setFromPlace(origen)
            plannerViewModel.setToPlace(destino)
            navController.popBackStack()
        },
        onClick = {
            val location = LocationDetail(
                description = it.properties.toCompactLabel(),
                address = "",
                latitude = it.geometry.coordinates[1],
                longitude = it.geometry.coordinates[0]
            )
            if (isOrigin) {
                plannerViewModel.setFromPlace(location)
            } else {
                plannerViewModel.setToPlace(location)
            }
            navController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseLocationScreenContent(
    isOrigin: Boolean,
    recentPlaces: List<RutaGuardadaDomain>,
    onLocationSelected: (com.felicks.sirbo.domain.Place) -> Unit,
    onNavigateToMap: () -> Unit,
    onDebugLocationClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onBackPressed: () -> Unit,
    onMyLocation: () -> Unit,
    onRecentLocationClick: (RutaGuardadaDomain) -> Unit,
    searchResults: List<PhotonFeature>,
    onClick: (PhotonFeature) -> Unit
) {
    Scaffold(
        topBar = {
//            SearchTopBar(
//                placeholder = "Busca un dirección. Ej. Avenida Carrasco",
//                searchQuery = searchQuery,
//                onSearchQueryChanged = onSearchQueryChanged,
//                isOrigin = isOrigin,
//                onBackPressed = onBackPressed
//            )
            SearchTopBar(
                placeholder = "Busca un dirección. Ej. Avenida Carrasco",
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                isOrigin = isOrigin,
                onBackPressed = onBackPressed,
                searchResults = searchResults,
                onResultClick = onClick
            )
        }
    ) { paddingValues ->
        when {
            searchQuery.isEmpty() -> {
                DefaultLocationList(
                    paddingValues,
                    recentPlaces = recentPlaces,
                    onLocationSelected = onLocationSelected,
                    onNavigateToMap = onNavigateToMap,
                    onDebugLocationClick = onDebugLocationClick,
                    onMyLocation = onMyLocation,
                    onRecentLocationClick = onRecentLocationClick
                )
            }

            else -> {
                LocationsResult(paddingValues, searchResults, onClick = {
                    Log.d("LocationsResult", "Selected feature: ${it.properties.toCompactLabel()}")
                    Log.d("LocationsResult", "Selected feature: ${it.geometry.coordinates}")
                    onClick(it)
//                    onLocationSelected(
//                        com.felicks.sirbo.domain.Place(
//                            name = it.properties.toCompactLabel(),
//                            latitud = it.geometry.coordinates[1],
//                            longitud = it.geometry.coordinates[0]
//                        )
//                    )
//                    navController.popBackStack() // Navigate back after selection
                })
            }
        }
    }
}

@Composable
fun LocationsResult(
    paddingValues: PaddingValues,
    searchResults: List<PhotonFeature>,
    onClick: (PhotonFeature) -> Unit = { }
) {
//    val searchResults = SpatialUtils.deduplicarFeaturesPorNombreYDistancia(searchResults, verbose = true)
    LazyColumn {
        items(searchResults) { feature ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(feature) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Search Result",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = feature.properties.toCompactLabel(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun DefaultLocationList(
    paddingValues: PaddingValues,
    onNavigateToMap: () -> Unit,
    onDebugLocationClick: () -> Unit,
    recentPlaces: List<RutaGuardadaDomain>,
    onMyLocation: () -> Unit,
    onRecentLocationClick: (RutaGuardadaDomain) -> Unit,
    onLocationSelected: (com.felicks.sirbo.domain.Place) -> Unit,
    viewModel: LocationViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        LocationOption(
            icon = Icons.Default.MyLocation,
            text = "Mi ubicación",
            onClick = onMyLocation,
        )

        LocationOption(
            icon = Icons.Default.LocationOn,
            text = "Seleccionar ubicación en el mapa",
            onClick = onNavigateToMap
        )
        if (com.felicks.sirbo.BuildConfig.DEBUG) {
            LocationOption(
                icon = Icons.Default.LocationOn,
                text = "Debug",
                onClick = onDebugLocationClick
            )
        }

        Text(
            text = "RECIENTES",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        if (recentPlaces.isNotEmpty()) {
            LazyColumn {
                items(recentPlaces) { location ->
                    RecentLocationItem(
                        place = location,
                        onClick = { onRecentLocationClick(location) }
                    )
                }
            }
        } else {
            Text(
                text = "No hay ubicaciones recientes",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
            )
        }
    }
}

@Composable
fun LocationOption(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick() })
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun RecentLocationItem(
    place: RutaGuardadaDomain,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Place, contentDescription = "Ubicación reciente")
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = place.direccionOrigen,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = place.direccionDestino,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray
                ),
            )
        }
        if (false) {
            IconButton(onClick = { /* Guardar ubicación como favorita */ }) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Guardar favorito",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewChooseLocationScreenContent() {
//    ChooseLocationScreenContent(
//        isOrigin = true,
//        recentPlaces = listOf(
//            Place("Avenida Espíritu Santo y Avenida Politécnico"),
//            Place("unknown place"),
//            Place("Rotonda Terminal Quillacollo y Avenida Blanco Galindo")
//        ),
//        onLocationSelected = {},
//        onNavigateToMap = {},
//        searchQuery = "",
//        onSearchQueryChanged = {},
//        onDebugLocationClick = {},
//        onBackPressed = {}
//    )
//}
//
