package com.felicks.sirbo.ui.navigation

import LocationsSelectionScreen
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.felicks.sirbo.ui.presentation.screens.ChooseLocationOnMapScreen
import com.felicks.sirbo.ui.presentation.screens.DetalleRutaScreen
import com.felicks.sirbo.ui.presentation.screens.mainScreens.ListaRutasScreen
import com.felicks.sirbo.ui.presentation.screens.mainScreens.PlanificaScreen
import com.felicks.sirbo.ui.presentation.screens.mainScreens.ProfileScreen
import com.felicks.sirbo.ui.presentation.screens.mainScreens.SettingsScreen
import com.felicks.sirbo.ui.presentation.screens.planner.AlternativeChooseLocationScreen
import com.felicks.sirbo.ui.presentation.screens.planner.OptimalRouteScreen
import com.felicks.sirbo.utils.MapConfig
import com.felicks.sirbo.viewmodel.HomeScreenViewModel
import com.felicks.sirbo.viewmodel.LocationViewModel
import com.felicks.sirbo.viewmodel.PlannerViewModel
import com.felicks.sirbo.viewmodel.RoutesViewModel

object MainDestinations {
    val items = listOf(
        Destinations.ListaDeRutasScreen,
        Destinations.PlanificaScreen,
        Destinations.ProfileScreen,
    )
}

@Composable
fun NavigationHost(
    bottomPadding: PaddingValues,
    plannerViewModel: PlannerViewModel = hiltViewModel(),
    navController: NavHostController,
    routesViewModel: RoutesViewModel = hiltViewModel(),
    homeScreenViewModel: HomeScreenViewModel,
    locationViewModel: LocationViewModel,
) {
    val saveableStateHolder = rememberSaveableStateHolder()
    val cameraPositionState = remember { MapConfig.initialState }

    // Guarda el índice actual de la pestaña visible
    var currentTabIndex by rememberSaveable { mutableStateOf(1) } // Por ejemplo, PlanificaScreen es índice 1


    NavHost(navController = navController, startDestination = Destinations.PlanificaScreen.route,
        enterTransition = {
            val newIndex = MainDestinations.items.indexOfFirst { it.route == this.targetState.destination.route }
            val direction = if (newIndex > currentTabIndex)
                AnimatedContentTransitionScope.SlideDirection.Left
            else
                AnimatedContentTransitionScope.SlideDirection.Right

            slideIntoContainer(direction, animationSpec = tween(350))
        },
        exitTransition = {
            val newIndex = MainDestinations.items.indexOfFirst { it.route == this.targetState.destination.route }
            val direction = if (newIndex > currentTabIndex)
                AnimatedContentTransitionScope.SlideDirection.Left
            else
                AnimatedContentTransitionScope.SlideDirection.Right

            slideOutOfContainer(direction, animationSpec = tween(350))
        },
        popEnterTransition = {
            val newIndex = MainDestinations.items.indexOfFirst { it.route == this.targetState.destination.route }
            val direction = if (newIndex < currentTabIndex)
                AnimatedContentTransitionScope.SlideDirection.Right
            else
                AnimatedContentTransitionScope.SlideDirection.Left

            slideIntoContainer(direction, animationSpec = tween(350))
        },

        popExitTransition = {
            val newIndex = MainDestinations.items.indexOfFirst { it.route == this.targetState.destination.route }
            val direction = if (newIndex < currentTabIndex)
                AnimatedContentTransitionScope.SlideDirection.Right
            else
                AnimatedContentTransitionScope.SlideDirection.Left

            slideOutOfContainer(direction, animationSpec = tween(350))
        }
    ) {

        composable(route = Destinations.ListaDeRutasScreen.route) { backStackEntry ->
            saveableStateHolder.SaveableStateProvider(Destinations.ListaDeRutasScreen.route) {
                currentTabIndex = 0
                ListaRutasScreen(
                    navController = navController,
                    bottomPadding = bottomPadding
                )
            }
        }

        composable(Destinations.PlanificaScreen.route) {
            saveableStateHolder.SaveableStateProvider(Destinations.PlanificaScreen.route) {
                currentTabIndex = 1
                PlanificaScreen(
                    viewModel = homeScreenViewModel,
                    navController = navController,
                    myCameraPositionState = cameraPositionState,
                    plannerViewModel = plannerViewModel,
                    navBarPadding = bottomPadding,
                )
            }
        }

        composable(
            Destinations.ProfileScreen.route,
        ) { navBackstateEntry ->
            currentTabIndex = 2
            ProfileScreen(
                navController = navController,
            )
        }

        composable(
            Destinations.SettingScreen.route,
        ) { navBackstateEntry ->
            SettingsScreen()
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

        composable(Destinations.LocationsSelectionScreen.route) {
            LocationsSelectionScreen(
                navController = navController,
                locationViewModel = locationViewModel,
            )

        }
        composable(
            route = Destinations.MapScreen.route + "{isOrigin}",
            arguments = listOf(navArgument("isOrigin") {
                type = NavType.BoolType
            })
        ) { navBackStack ->
            val isOrigin = navBackStack.arguments?.getBoolean("isOrigin")
            ChooseLocationOnMapScreen(
                isOrigin = isOrigin!!,
                locationViewModel = locationViewModel,
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