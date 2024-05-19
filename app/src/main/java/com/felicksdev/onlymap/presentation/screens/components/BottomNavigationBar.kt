package com.felicksdev.onlymap.presentation.screens.components
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.felicksdev.onlymap.navigation.Destinations

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<Destinations>
) {
    val currenRoute = currentRoute(navController = navController)
    BottomNavigation {
        items.forEach { screem ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screem.icon, contentDescription = screem.title) },
                label = { Text(text = screem.title) },
                selected = currenRoute == screem.route  ,
                onClick = {
                    navController.navigate(screem.route) {
                        popUpTo(navController.graph.findStartDestination().id){
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
@Composable
private fun currentRoute (navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}