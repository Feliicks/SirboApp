package com.felicksdev.onlymap.presentation.components.bottomBars

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.MAIN_DESTINATIONS
import com.felicksdev.onlymap.utils.currentRoute

@Composable
fun BottomNavigationBar(
    navController: NavController,
) {
    val items = MAIN_DESTINATIONS
    val currentRoute = currentRoute(navController = navController)
    if (items.any { it.route == currentRoute }) {
        NavigationBar {
            items.forEach { destination ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.title
                        )
                    },
                    label = { Text(text = destination.title) },
                    selected = currentRoute == destination.route,
                    onClick = {
                        navController.navigate(destination.route) {
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


@Preview
@Composable
fun BottomNavigationBarPreview(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val items = MAIN_DESTINATIONS
    NavigationBar {
        items.forEach { destination ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.title
                    )
                },
                label = { Text(text = destination.title) },
                selected = todo(),
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                alwaysShowLabel = false
            )
        }
//        }
    }
}

fun todo(): Boolean = true
