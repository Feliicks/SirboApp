package com.felicksdev.onlymap.navigation

import LocationsSelectionScreen
import RutasViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.felicksdev.onlymap.RoutesScreen
import com.felicksdev.onlymap.core.RetrofitHelper
import com.felicksdev.onlymap.core.RetrofitHelper.getRetrofit
import com.felicksdev.onlymap.data.models.Ruta
import com.felicksdev.onlymap.data.network.MyApiService
import com.felicksdev.onlymap.navigation.Destinations.*
import com.felicksdev.onlymap.screens.HomeScreen
import com.felicksdev.onlymap.screens.MapScreen
import com.felicksdev.onlymap.screens.SecondScreen
import com.felicksdev.onlymap.screens.ThirdScreen
import com.felicksdev.onlymap.screens.SelectLocationScreen
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel


@Composable
fun NavigationHost(
    navController: NavHostController
) {
    //val rutaViewModel by viewModel<RutasViewModel>()
    val rutasViewModel =  RutasViewModel()
    val homeScreenViewModel = HomeScreenViewModel()
    //val helper = getRetrofit().create(MyApiService::class.java).getLinea("ruta/nombre/${nombre}")
    NavHost(navController = navController, startDestination = HomeScreen.route) {
        composable(HomeScreen.route) {3
            HomeScreen(
                navegarPantalla2 = { newText ->
                    navController.navigate(SecondScreen.createRoute(newText))
                },
                viewModel = homeScreenViewModel,
                navController = navController
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
            LocationsSelectionScreen(onNextClick =  {_, _ -> }, navController = navController)
        }

        composable(LocationsSelectionScreen.route) {

            //val listaRutas: List<Ruta> = getRetrofit().getAllRutas().body()!! // Obtener tu lista de rutas, por ejemplo, desde tu ViewModel o donde sea necesario
            LocationsSelectionScreen(onNextClick =  {_, _ -> }, navController = navController)
        }
        composable(MapScreen.route) {

            //val listaRutas: List<Ruta> = getRetrofit().getAllRutas().body()!! // Obtener tu lista de rutas, por ejemplo, desde tu ViewModel o donde sea necesario
            MapScreen()
        }
//        composable(RoutesScreen.route) {
//            RoutesScreen(viewModel = RutasViewModel(helper), navController = navController)
//        }
    }
}