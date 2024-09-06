package com.felicksdev.onlymap

import RutasViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.Destinations.HomeScreen
import com.felicksdev.onlymap.navigation.Destinations.SecondScreen
import com.felicksdev.onlymap.navigation.Destinations.ThirdScreen
import com.felicksdev.onlymap.navigation.NavigationHost
import com.felicksdev.onlymap.presentation.components.BottomNavigationBar
import com.felicksdev.onlymap.presentation.components.RouterPlannerBar
import com.felicksdev.onlymap.utils.currentRoute
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.felicksdev.onlymap.viewmodel.MainViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel

@Composable
fun SirboApp() {
    val navController = rememberNavController()
    val rutasViewModel = RutasViewModel()
    val homeScreenViewModel = HomeScreenViewModel()
    val locationViewModel = LocationViewModel()
    val mainViewModel = MainViewModel()
    val plannerViewModel = viewModel<PlannerViewModel>()
//        val plannerViewModel : PlannerViewModel by viewModels()
    val navigationItems = listOf(
        SecondScreen,
        HomeScreen,
        ThirdScreen
    )
    val currentRoute = currentRoute(navController = navController)
    Scaffold(
        topBar = {
            //TODO Validar si esta en la pantalla de inicio para mostrar el topBar
//                si no no mostgar en ninguna pantalla
            if (currentRoute == HomeScreen.route) {
                RouterPlannerBar(
                    plannerViewModel = plannerViewModel,
                    navController = navController
                )
            }
        },
//                TODO
//            El bottom bar solo disponible para las tres ventanas pricnipales
        bottomBar = {
            if (navigationItems.any { it.route == currentRoute }) {
                BottomNavigationBar(
                    items = navigationItems,
                    navController = navController
                )
            }

        }

    ) { padding ->
        NavigationHost(
            plannerViewModel = plannerViewModel,
            navController = navController, padding,
            rutasViewModel = rutasViewModel,
            homeScreenViewModel = homeScreenViewModel,
            locationViewModel = locationViewModel,
            mainViewModel = mainViewModel
        )
    }
}