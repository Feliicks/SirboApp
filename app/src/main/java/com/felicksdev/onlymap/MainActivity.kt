package com.felicksdev.onlymap

import RutasViewModel
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.databinding.ActivityMain2Binding
import com.felicksdev.onlymap.navigation.Destinations.HomeScreen
import com.felicksdev.onlymap.navigation.Destinations.SecondScreen
import com.felicksdev.onlymap.navigation.Destinations.ThirdScreen
import com.felicksdev.onlymap.navigation.NavigationHost
import com.felicksdev.onlymap.presentation.components.BottomNavigationBar
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.felicksdev.onlymap.viewmodel.MainViewModel

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
        val navController = rememberNavController()
        val rutasViewModel = RutasViewModel()
        val homeScreenViewModel = HomeScreenViewModel()
        val locationViewModel = LocationViewModel()
        val mainViewModel = MainViewModel()

        val navigationItems = listOf(
            SecondScreen,
            HomeScreen,
            ThirdScreen
        )
        Scaffold(
            bottomBar = {

                    BottomNavigationBar(
                        items = navigationItems,
                        navController = navController
                    )

            }

        ) { padding ->
            NavigationHost(
                navController = navController, padding,
                rutasViewModel = rutasViewModel,
                homeScreenViewModel = homeScreenViewModel,
                locationViewModel = locationViewModel,
                mainViewModel = mainViewModel
            )
        }
    }


}