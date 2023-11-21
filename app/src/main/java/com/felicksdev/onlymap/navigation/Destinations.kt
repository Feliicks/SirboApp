package com.felicksdev.onlymap.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title : String,
    val icon : ImageVector
){
    object HomeScreen: Destinations(
        "home_screen",
        "Home",
        Icons.Filled.Home
    )
    object SecondScreen: Destinations(
        "routes_screen/?newText={newText}",
        "Routes",
        Icons.Filled.Settings
    ){
        fun createRoute(text: String) = "routes_screen/?newText=$text"
    }
    object ThirdScreen: Destinations(
        "addresses_screen",
        "Addresses",
        Icons.Filled.Favorite

    )
}
