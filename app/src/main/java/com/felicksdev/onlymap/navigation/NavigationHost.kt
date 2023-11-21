package com.felicksdev.onlymap.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.felicksdev.onlymap.navigation.Destinations.HomeScreen
import com.felicksdev.onlymap.navigation.Destinations.SecondScreen
import com.felicksdev.onlymap.navigation.Destinations.ThirdScreen
import com.felicksdev.onlymap.screens.HomeScreen
import com.felicksdev.onlymap.screens.SecondScreen
import com.felicksdev.onlymap.screens.ThirdScreen
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel


@Composable
fun NavigationHost(
    navController: NavHostController
) {
    val homeScreenViewModel = HomeScreenViewModel()
    NavHost(navController = navController, startDestination = HomeScreen.route) {
        composable(HomeScreen.route) {3
            HomeScreen(
                navegarPantalla2 = { newText ->
                    navController.navigate(SecondScreen.createRoute(newText))
                },
                viewModel = homeScreenViewModel
            )
        }
        composable(
            SecondScreen.route,
            arguments = listOf(navArgument("newText") { defaultValue = "defaultYext" })
        ) { navBackstateEntry ->
            var newText = navBackstateEntry.arguments?.getString("newText")
            requireNotNull(newText)
            SecondScreen(newText)
        }
        composable(ThirdScreen.route) {
            ThirdScreen()
        }
    }
}