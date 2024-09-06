package com.felicksdev.onlymap.navigation

import ChooseLocationsScreen
import LocationsSelectionScreen
import RutasViewModel
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
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
import com.felicksdev.onlymap.presentation.screens.OptimalRoutesScreen
import com.felicksdev.onlymap.presentation.screens.RouteDetailScreen
import com.felicksdev.onlymap.presentation.screens.main.HomeScreen
import com.felicksdev.onlymap.presentation.screens.main.SecondScreen
import com.felicksdev.onlymap.presentation.screens.main.ThirdScreen
import com.felicksdev.onlymap.utils.MapConfig
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.felicksdev.onlymap.viewmodel.MainViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel


@Composable
fun NavigationHost(
    plannerViewModel: PlannerViewModel,
    navController: NavHostController,
    paddings: PaddingValues,
    rutasViewModel: RutasViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    locationViewModel: LocationViewModel,
    mainViewModel: MainViewModel
) {
    val cameraPositionState = remember { MapConfig.initialState }
    NavHost(navController = navController, startDestination = HomeScreen.route) {
        composable(HomeScreen.route) {
            HomeScreen(
                viewModel = homeScreenViewModel,
                navController = navController,
                innerPadding = paddings,
                cameraPositionState = cameraPositionState
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
                navController = navController
            )
        }
        composable(Destinations.OptimalRoutesScreen.route) {
            OptimalRoutesScreen(
                mainViewModel = mainViewModel,
                locationViewModel = locationViewModel,
                rutasViewModel = rutasViewModel,
                navController = navController,
                paddingValues = paddings

            )
        }
        composable(LocationsSelectionScreen.route) {
            LocationsSelectionScreen(
                navController = navController,
                locationViewModel = locationViewModel,
                routesViewModel = rutasViewModel
            )
        }
        composable(
            route = MapScreen.route + "{isOrigin}",
            arguments = listOf(navArgument("isOrigin") {
                type = NavType.BoolType
            })
        ) {
            navBackStack ->
            val isOrigin = navBackStack.arguments?.getBoolean("isOrigin")
            MapScreen(
                isOrigin = isOrigin!!,
                viewModel = locationViewModel,
                cameraPositionState = cameraPositionState
            )
        }

        composable(
            route = Destinations.ChooseLocations.route + "{isOrigin}",
            arguments = listOf(navArgument("isOrigin") {
                defaultValue = false
                type = NavType.BoolType
            })
        ) { backStackEntry ->
            val isOrigin = backStackEntry.arguments?.getBoolean("isOrigin")
            Log.d("NavigationHost", "isOrigin: $isOrigin")
            ChooseLocationsScreen(
                isOrigin = isOrigin!!,
                navController = navController,
                plannerViewModel = plannerViewModel
            )
        }

    }
}