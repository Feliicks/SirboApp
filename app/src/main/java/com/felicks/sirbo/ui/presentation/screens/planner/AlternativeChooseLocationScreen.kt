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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.felicks.sirbo.domain.Place
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
    val recentLocations by viewModel.recentLocations.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val recentPlaces by viewModel.recentPlaces.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    ChooseLocationScreenContent(
        isOrigin = isOrigin,
        recentPlaces = recentLocations,
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
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseLocationScreenContent(
    isOrigin: Boolean,
    recentPlaces: List<com.felicks.sirbo.domain.Place>,
    onLocationSelected: (com.felicks.sirbo.domain.Place) -> Unit,
    onNavigateToMap: () -> Unit,
    onDebugLocationClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            SearchTopBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                isOrigin = isOrigin,
                onBackPressed = onBackPressed
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

                    )
            }

            else -> {
                LocationsResult(paddingValues)
            }
        }
    }
}

@Composable
fun LocationsResult(
    paddingValues: PaddingValues
) {
//    TODO("Not yet implemented")
}

@Composable
fun DefaultLocationList(
    paddingValues: PaddingValues,
    onNavigateToMap: () -> Unit,
    onDebugLocationClick: () -> Unit,
    recentPlaces: List<com.felicks.sirbo.domain.Place>,
    onLocationSelected: (com.felicks.sirbo.domain.Place) -> Unit,
) {
    Column(modifier = Modifier.padding(paddingValues)) {
        LocationOption(
            icon = Icons.Default.MyLocation,
            text = "Su ubicación",
            onClick = {
                Log.d("LocationsOptions", "Mi ubicación")
            },
        )

        LocationOption(
            icon = Icons.Default.LocationOn,
            text = "Seleccionar ubicación en el mapa",
            onClick = onNavigateToMap
        )
        LocationOption(
            icon = Icons.Default.LocationOn,
            text = "Debug",
            onClick = onDebugLocationClick
        )

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
                        onClick = { onLocationSelected(location) }
                    )
                }
            }
        }
    }
}

@Composable
fun LocationOption(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
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
    place: Place,
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
        Text(
            text = place.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { /* Guardar ubicación como favorita */ }) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Guardar favorito",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChooseLocationScreenContent() {
    ChooseLocationScreenContent(
        isOrigin = true,
        recentPlaces = listOf(
            Place("Avenida Espíritu Santo y Avenida Politécnico"),
            Place("unknown place"),
            Place("Rotonda Terminal Quillacollo y Avenida Blanco Galindo")
        ),
        onLocationSelected = {},
        onNavigateToMap = {},
        searchQuery = "",
        onSearchQueryChanged = {},
        onDebugLocationClick = {},
        onBackPressed = {}
    )
}

