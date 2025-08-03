package com.felicks.sirbo.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object PlanificaScreen : Destinations(
        "home_screen",
        "Planifica",
        Icons.Filled.Route
    )

    object FavoritesScreen : Destinations(
        "routes_screen/?newText={newText}",
        "Favoritos",
        Icons.Filled.Favorite
    )

    object ListaDeRutasScreen : Destinations(
        "addresses_screen",
        "Rutas",
        Icons.Filled.Directions
    )
    object Settingscreen : Destinations(
        "settings_screen",
        "Configuraciones",
        Icons.Filled.Settings
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
