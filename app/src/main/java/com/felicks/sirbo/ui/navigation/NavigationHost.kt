package com.felicks.sirbo.ui.navigation

import LocationsSelectionScreen
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.felicks.sirbo.ui.navigation.Destinations.FavoritesScreen
import com.felicks.sirbo.ui.navigation.Destinations.ListaDeRutasScreen
import com.felicks.sirbo.ui.navigation.Destinations.LocationsSelectionScreen
import com.felicks.sirbo.ui.navigation.Destinations.MapScreen
import com.felicks.sirbo.ui.navigation.Destinations.PlanificaScreen
import com.felicks.sirbo.ui.presentation.screens.ChooseLocationOnMapScreen
import com.felicks.sirbo.ui.presentation.screens.DetalleRutaScreen
import com.felicks.sirbo.ui.presentation.screens.mainScreens.ListaRutasScreen
import com.felicks.sirbo.ui.presentation.screens.mainScreens.PlanificaScreen
import com.felicks.sirbo.ui.presentation.screens.mainScreens.SecondScreen
import com.felicks.sirbo.ui.presentation.screens.planner.AlternativeChooseLocationScreen
import com.felicks.sirbo.ui.presentation.screens.planner.OptimalRouteScreen
import com.felicks.sirbo.utils.MapConfig
import com.felicks.sirbo.viewmodel.HomeScreenViewModel
import com.felicks.sirbo.viewmodel.LocationViewModel
import com.felicks.sirbo.viewmodel.PlannerViewModel
import com.felicks.sirbo.viewmodel.RoutesViewModel

val MAIN_DESTINATIONS = listOf(
//    FavoritesScreen,
    PlanificaScreen,
    ListaDeRutasScreen
)

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
    NavHost(navController = navController, startDestination = PlanificaScreen.route,
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
        composable(PlanificaScreen.route) {
            saveableStateHolder.SaveableStateProvider(PlanificaScreen.route) {
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
            FavoritesScreen.route,
        ) { navBackstateEntry ->
            SecondScreen(
                navController = navController,
                bottomPadding = bottomPadding
            )
        }
        composable(route = ListaDeRutasScreen.route) { backStackEntry ->
            saveableStateHolder.SaveableStateProvider(ListaDeRutasScreen.route) {
                ListaRutasScreen(
                    navController = navController,
                    bottomPadding = bottomPadding
                )
            }
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