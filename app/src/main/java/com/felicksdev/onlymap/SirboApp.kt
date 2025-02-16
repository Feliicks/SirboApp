package com.felicksdev.onlymap

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.ui.navigation.NavigationHost
import com.felicksdev.onlymap.ui.presentation.components.bottomBars.BottomNavigationBar
import com.felicksdev.onlymap.ui.theme.OnlyMapTheme
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel

@Composable
fun SirboApp() {
    val plannerViewModel: PlannerViewModel = hiltViewModel()
//    Se puede eliminar sintacia usano DI

    val navController = rememberNavController()
    val homeScreenViewModel = HomeScreenViewModel()
    val locationViewModel = LocationViewModel()
    val plannerState = plannerViewModel.plannerState.collectAsState()
    OnlyMapTheme(

    ) {
        Scaffold(bottomBar = {
            if (plannerState.value.isPlacesDefined)
                CustomBottomSheet()
            else
                BottomNavigationBar(navController = navController)
        }) { bottomPadding ->
            NavigationHost(
                bottomPadding = bottomPadding,
                plannerViewModel = plannerViewModel,
                navController = navController,
                homeScreenViewModel = homeScreenViewModel,
                locationViewModel = locationViewModel
            )
        }
    }
}

@Composable
fun CustomBottomSheet() {

}
