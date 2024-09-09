package com.felicksdev.onlymap

import RutasViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.NavigationHost
import com.felicksdev.onlymap.presentation.components.bottomBars.BottomNavigationBar
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
//   val plannerViewModel : PlannerViewModel by viewModels()
//      Esta instancia solo se aplicaría si se estuviera
//      trabajando Dentro de un Fragment o Activity
    val plannerState = plannerViewModel.plannerState.collectAsState()
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
            rutasViewModel = rutasViewModel,
            homeScreenViewModel = homeScreenViewModel,
            locationViewModel = locationViewModel,
            mainViewModel = mainViewModel
        )
    }
}

@Composable
fun CustomBottomSheet() {

}
