package com.felicksdev.onlymap.navigation

import LocationsSelectionScreen
import RutasViewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.felicksdev.onlymap.navigation.Destinations.HomeScreen
import com.felicksdev.onlymap.navigation.Destinations.LocationsSelectionScreen
import com.felicksdev.onlymap.navigation.Destinations.MapScreen
import com.felicksdev.onlymap.navigation.Destinations.RouteDetailScreen
import com.felicksdev.onlymap.navigation.Destinations.SecondScreen
import com.felicksdev.onlymap.navigation.Destinations.ThirdScreen
import com.felicksdev.onlymap.presentation.screens.MapScreen
import com.felicksdev.onlymap.presentation.screens.RouteDetailScreen
import com.felicksdev.onlymap.presentation.screens.main.HomeScreen
import com.felicksdev.onlymap.presentation.screens.main.SecondScreen
import com.felicksdev.onlymap.presentation.screens.main.ThirdScreen
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.LocationViewModel


@Composable
fun NavigationHost(
    navController: NavHostController,
    paddings: PaddingValues,
    rutasViewModel : RutasViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    locationViewModel : LocationViewModel

) {
//    val rutasViewModel = RutasViewModel()
//    val homeScreenViewModel = HomeScreenViewModel()
//    val locationViewModel = LocationViewModel()
    NavHost(navController = navController, startDestination = HomeScreen.route) {
        composable(HomeScreen.route) {

            HomeScreen(
                viewModel = homeScreenViewModel,
                navController = navController,
                defaultPadding = paddings
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
            ThirdScreen(
                viewModel = rutasViewModel,
                defaultPadding = paddings,
                navController = navController
            )
        }
        composable(RouteDetailScreen.route) {
            RouteDetailScreen(
                ruta = rutasViewModel.routeSelected,
                viewModel = rutasViewModel,
                padding = paddings,
//                navController = navController
            )
        }
        composable(LocationsSelectionScreen.route) {
            LocationsSelectionScreen(
                onNextClick = { _, _ -> },
                navController = navController,
                locationViewModel = locationViewModel
            )
        }
        composable(MapScreen.route) {
            MapScreen(locationViewModel)
        }

    }
}