package com.felicksdev.onlymap.navigation

import ChooseLocationsScreen
import LocationsSelectionScreen
import RutasViewModel
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.LayoutDirection
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

val MAIN_DESTINATIONS = listOf(
    SecondScreen,
    HomeScreen,
    ThirdScreen
)

@Composable
fun NavigationHost(
    bottomPadding: PaddingValues,
    plannerViewModel: PlannerViewModel,
    navController: NavHostController,
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
                cameraPositionState = cameraPositionState,
                plannerViewModel = plannerViewModel,
                bottomPadding = bottomPadding
            )
        }
        composable(
            SecondScreen.route,
        ) { navBackstateEntry ->
            SecondScreen(
                navController = navController,
                bottomPadding = bottomPadding
            )
        }
        composable(ThirdScreen.route) {
            ThirdScreen(
                viewModel = rutasViewModel,
                navController = navController,
                bottomPadding = bottomPadding
            )
        }
        composable(RouteDetailScreen.route) {
            RouteDetailScreen(
                ruta = rutasViewModel.routeSelected,
                viewModel = rutasViewModel,
                navController = navController
            )
        }
        composable(Destinations.OptimalRoutesScreen.route) {
            OptimalRoutesScreen(
                mainViewModel = mainViewModel,
                locationViewModel = locationViewModel,
                rutasViewModel = rutasViewModel,
                navController = navController,
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
        ) { navBackStack ->
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


operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = PaddingValues(
    start = this.calculateStartPadding(LayoutDirection.Ltr) +
            other.calculateStartPadding(LayoutDirection.Ltr),
    top = this.calculateTopPadding() + other.calculateTopPadding(),
    end = this.calculateEndPadding(LayoutDirection.Ltr) +
            other.calculateEndPadding(LayoutDirection.Ltr),
    bottom = this.calculateBottomPadding() + other.calculateBottomPadding(),
)