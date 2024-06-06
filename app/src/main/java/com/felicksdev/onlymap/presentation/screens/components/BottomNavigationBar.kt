package com.felicksdev.onlymap.presentation.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.felicksdev.onlymap.navigation.Destinations
import com.felicksdev.onlymap.utils.currentRoute

@Composable
fun BottomNavigationBar(
//    bottomBarState: MutableState<Boolean>,
    navController: NavHostController,
    items: List<Destinations>
) {
    val currenRoute = currentRoute(navController = navController)

    val bottomBarState = rememberSaveable { mutableStateOf(true) }
    when (currenRoute) {
        Destinations.RouteDetailScreen.route -> {
            // Show BottomBar and TopBar
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

@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}