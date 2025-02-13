package com.felicksdev.onlymap.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object HomeScreen : Destinations(
        "home_screen",
        "Inicio",
        Icons.Filled.Home
    )

    object SecondScreen : Destinations(
        "routes_screen/?newText={newText}",
        "Routes",
        Icons.Filled.Settings
    ) {
        fun createRoute(text: String) = "routes_screen/?newText=$text"
    }

    object ThirdScreen : Destinations(
        "addresses_screen",
        "Rutas",
        Icons.Filled.LocationOn
    )
    object RouteDetailScreen : Destinations(
        "route_detail_screen",
        "Rutas",
        Icons.Filled.Favorite
    )
    object OptimalRoutesScreen : Destinations(
        "optimal_routes_screen",
        "rutas optimas",
        Icons.Filled.Favorite
    )

    object LocationsSelectionScreen : Destinations(
        "locations_screen",
        "Locations",
        Icons.Filled.Favorite
    )

    object RoutesScreen : Destinations(
        "routes_screen",
        "Rutas",
        Icons.Filled.Favorite
    )
    object MapScreen : Destinations(
        "map_screen/",
        "Mapa",
        Icons.Filled.Favorite
    )
    object ChooseLocations : Destinations(
        "search/",
        "Mapa",
        Icons.Filled.Favorite
    )
}
