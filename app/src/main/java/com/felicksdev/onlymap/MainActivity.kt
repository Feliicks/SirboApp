package com.felicksdev.onlymap

import RutasViewModel
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.databinding.ActivityMain2Binding
import com.felicksdev.onlymap.navigation.Destinations.HomeScreen
import com.felicksdev.onlymap.navigation.Destinations.SecondScreen
import com.felicksdev.onlymap.navigation.Destinations.ThirdScreen
import com.felicksdev.onlymap.navigation.NavigationHost
import com.felicksdev.onlymap.presentation.screens.components.BottomNavigationBar
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.LocationViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding

    // TODO  Validacion de que si el eusautio tiene permisos de ubicacion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            SirboApp()
        }
    }

    @Composable
    fun SirboApp() {
//        var viewModel = MainViewModel()
//        val showBottomBar by viewModel.showBottomBar.collectAsState()
        val navController = rememberNavController()
        val rutasViewModel = RutasViewModel()
        val homeScreenViewModel = HomeScreenViewModel()
        val locationViewModel = LocationViewModel()
        val bottomBarState = rememberSaveable{mutableStateOf(true)}
        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        when (navBackStackEntry?.destination?.route) {
//            RouteDetailScreen.route -> {
//                // Show BottomBar and TopBar
//                bottomBarState.value = false
//
//            }
//            else -> {
//                bottomBarState.value = true
//
//            }
//        }
        val navigationItems = listOf(
            SecondScreen,
            HomeScreen,
            ThirdScreen
        )
        Scaffold(
            bottomBar = {

                    BottomNavigationBar(
//                        bottomBarState = bottomBarState,
                        items = navigationItems,
                        navController = navController
                    )

            }

        ) { padding ->
            NavigationHost(
                navController = navController, padding,
                rutasViewModel = rutasViewModel,
                homeScreenViewModel = homeScreenViewModel,
                locationViewModel = locationViewModel
            )
        }
    }


}