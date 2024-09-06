package com.felicksdev.onlymap.presentation.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.felicksdev.onlymap.navigation.MAIN_DESTINATIONS
import com.felicksdev.onlymap.utils.currentRoute

@Composable
fun BottomNavigationBar(
    navController: NavController,
) {
    val items = MAIN_DESTINATIONS
    val currenRoute = currentRoute(navController = navController)
//    Ahora aqui quiero que solo sevea el bottom bar en las MAIN DESRTINATIONS
//    pero esa logica ya la tenia hecha
    if (items.any { it.route == currenRoute }) {
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

@Preview
@Composable
fun BottomNavigationBarPreview(modifier: Modifier = Modifier) {
    BottomNavigationBar(
        navController = NavHostController(LocalContext.current),
    )
}