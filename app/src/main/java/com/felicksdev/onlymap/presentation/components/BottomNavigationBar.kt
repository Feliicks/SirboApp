package com.felicksdev.onlymap.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.felicksdev.onlymap.navigation.Destinations
import com.felicksdev.onlymap.navigation.Destinations.LocationsSelectionScreen
import com.felicksdev.onlymap.navigation.Destinations.MapScreen
import com.felicksdev.onlymap.navigation.Destinations.OptimalRoutesScreen
import com.felicksdev.onlymap.navigation.Destinations.RouteDetailScreen
import com.felicksdev.onlymap.utils.currentRoute

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<Destinations>
) {
    val currenRoute = currentRoute(navController = navController)

    val bottomBarState = rememberSaveable { mutableStateOf(true) }
    when (currenRoute) {
        RouteDetailScreen.route -> {
            bottomBarState.value = false
        }
        LocationsSelectionScreen.route -> {
            bottomBarState.value = false
        }
        MapScreen.route -> {
            bottomBarState.value = false
        }
        OptimalRoutesScreen.route -> {
            bottomBarState.value = false
        }
        else -> {
            bottomBarState.value = true
        }
    }
    AnimatedVisibility(visible = bottomBarState.value) {
        BottomNavigation {
            items.forEach { screem ->
                BottomNavigationItem(
                    icon = { Icon(imageVector = screem.icon, contentDescription = screem.title) },
                    label = { Text(text = screem.title) },
                    selected = currenRoute == screem.route,
                    onClick = {
                        navController.navigate(screem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}