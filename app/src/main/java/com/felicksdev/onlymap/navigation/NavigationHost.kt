package com.felicksdev.onlymap.navigation

import LocationsSelectionScreen
import RutasViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.felicksdev.onlymap.navigation.Destinations.HomeScreen
import com.felicksdev.onlymap.navigation.Destinations.LocationsSelectionScreen
import com.felicksdev.onlymap.navigation.Destinations.MapScreen
import com.felicksdev.onlymap.navigation.Destinations.SecondScreen
import com.felicksdev.onlymap.navigation.Destinations.ThirdScreen
import com.felicksdev.onlymap.presentation.screens.HomeScreen
import com.felicksdev.onlymap.presentation.screens.MapScreen
import com.felicksdev.onlymap.presentation.screens.SecondScreen
import com.felicksdev.onlymap.presentation.screens.ThirdScreen
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.LocationViewModel


@Composable
fun NavigationHost(
    navController: NavHostController
) {
    //val rutaViewModel by viewModel<RutasViewModel>()
    val rutasViewModel = RutasViewModel()
    val homeScreenViewModel = HomeScreenViewModel()
    val locationViewModel = LocationViewModel()

    //val helper = getRetrofit().create(MyApiService::class.java).getLinea("ruta/nombre/${nombre}")
    NavHost(navController = navController, startDestination = HomeScreen.route) {
        composable(HomeScreen.route) {
            3
            HomeScreen(
                viewModel = homeScreenViewModel,
                navController = navController,
                locationViewModel = locationViewModel
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

            //val listaRutas: List<Ruta> = getRetrofit().getAllRutas().body()!! // Obtener tu lista de rutas, por ejemplo, desde tu ViewModel o donde sea necesario
            ThirdScreen(viewModel = rutasViewModel)
        }
        composable(LocationsSelectionScreen.route) {

            //val listaRutas: List<Ruta> = getRetrofit().getAllRutas().body()!! // Obtener tu lista de rutas, por ejemplo, desde tu ViewModel o donde sea necesario
            LocationsSelectionScreen(
                onNextClick = { _, _ -> },
                navController = navController,
                locationViewModel = locationViewModel
            )
        }
        composable(MapScreen.route) {

            //val listaRutas: List<Ruta> = getRetrofit().getAllRutas().body()!! // Obtener tu lista de rutas, por ejemplo, desde tu ViewModel o donde sea necesario
            MapScreen(locationViewModel)
        }
//        composable(RoutesScreen.route) {
//            RoutesScreen(viewModel = RutasViewModel(helper), navController = navController)
//        }
    }
}