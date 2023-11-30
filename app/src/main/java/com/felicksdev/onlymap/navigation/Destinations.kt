package com.felicksdev.onlymap.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object HomeScreen : Destinations(
        "home_screen",
        "Home",
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
        "map_screen",
        "Mapa",
        Icons.Filled.Favorite
    )
}
