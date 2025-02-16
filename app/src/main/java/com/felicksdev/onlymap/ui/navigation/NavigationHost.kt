package com.felicksdev.onlymap.ui.navigation

import LocationsSelectionScreen
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.felicksdev.onlymap.ui.navigation.Destinations.HomeScreen
import com.felicksdev.onlymap.ui.navigation.Destinations.LocationsSelectionScreen
import com.felicksdev.onlymap.ui.navigation.Destinations.MapScreen
import com.felicksdev.onlymap.ui.navigation.Destinations.SecondScreen
import com.felicksdev.onlymap.ui.presentation.screens.DetalleRutaScreen
import com.felicksdev.onlymap.ui.presentation.screens.ChooseLocationOnMapScreen
import com.felicksdev.onlymap.ui.presentation.screens.mainScreens.HomeScreen
import com.felicksdev.onlymap.ui.presentation.screens.mainScreens.ListaRutasScreen
import com.felicksdev.onlymap.ui.presentation.screens.mainScreens.SecondScreen
import com.felicksdev.onlymap.ui.presentation.screens.planner.AlternativeChooseLocationScreen
import com.felicksdev.onlymap.ui.presentation.screens.planner.OptimalRouteScreen
import com.felicksdev.onlymap.utils.MapConfig
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel
import com.felicksdev.onlymap.viewmodel.RoutesViewModel

val MAIN_DESTINATIONS = listOf(
    SecondScreen,
    HomeScreen,
    Destinations.ThirdScreen
)

@Composable
fun NavigationHost(
    bottomPadding: PaddingValues,
    plannerViewModel: PlannerViewModel,
    navController: NavHostController,
    routesViewModel: RoutesViewModel = hiltViewModel(),
    homeScreenViewModel: HomeScreenViewModel,
    locationViewModel: LocationViewModel,

    ) {
    val cameraPositionState = remember { MapConfig.initialState }
    NavHost(navController = navController, startDestination = HomeScreen.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(350)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(350)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(350)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(350)
            )
        }) {
        composable(HomeScreen.route) {
            HomeScreen(
                viewModel = homeScreenViewModel,
                navController = navController,
                myCameraPositionState = cameraPositionState,
                plannerViewModel = plannerViewModel,
                bottomPadding = bottomPadding,
                routesViewModel = routesViewModel
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
        composable(route = Destinations.ThirdScreen.route) { backStackEntry ->
//            val routeId = backStackEntry.arguments?.getString("routeId")
            ListaRutasScreen(
                navController = navController,
                bottomPadding = bottomPadding
            )
        }
        composable(Destinations.RouteDetailScreen.route + "{id}") { backStrackEntry ->
            val id = backStrackEntry.arguments?.getString("id").toString()
            val TAG = "NavigationHost"
            Log.d(TAG, "id de ruta en nav: $id")
            DetalleRutaScreen(
                idRuta = id,
                navController = navController,
            )
        }
        composable(Destinations.OptimalRoutesScreen.route) {
            OptimalRouteScreen(
                plannerViewModel = plannerViewModel,
                locationViewModel = locationViewModel,

                navController = navController,
            )
        }
        composable(LocationsSelectionScreen.route) {
            LocationsSelectionScreen(
                navController = navController,
                locationViewModel = locationViewModel,
            )

        }
        composable(
            route = MapScreen.route + "{isOrigin}",
            arguments = listOf(navArgument("isOrigin") {
                type = NavType.BoolType
            })
        ) { navBackStack ->
            val isOrigin = navBackStack.arguments?.getBoolean("isOrigin")
            ChooseLocationOnMapScreen(
                isOrigin = isOrigin!!,
                viewModel = locationViewModel,
                cameraPositionState = cameraPositionState,
                navController = navController,
                plannerViewModel = plannerViewModel
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
//            ChooseLocationsScreen(
//                isOrigin = isOrigin!!,
//                navController = navController,
//                plannerViewModel = plannerViewModel
//            )

            AlternativeChooseLocationScreen(
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