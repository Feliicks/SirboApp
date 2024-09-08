package com.felicksdev.onlymap

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.navigation.Destinations.HomeScreen
import com.felicksdev.onlymap.navigation.Destinations.SecondScreen
import com.felicksdev.onlymap.navigation.Destinations.ThirdScreen
import com.felicksdev.onlymap.presentation.components.BottomNavigationBar
import com.felicksdev.onlymap.viewmodel.LocationViewModel

class MainActivity2 : AppCompatActivity() {
    // TODO  Validacion de que si el eusautio tiene permisos de ubicacion

    //val rutasViewModel = viewModels<> {  }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var locationViewModel = LocationViewModel()

        setContent {
//            NavigationHost()
            MainScreen()
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        val navigationItems = listOf(
            SecondScreen,
            HomeScreen,
            ThirdScreen
        )
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                )
            }

        ){
//            NavigationHost(navController = navController,it)
//            HomeScreen(navegarPantalla2 = { newText ->
//                navController.navigate(SecondScreen.createRoute(newText))
//            })
        }
    }

    private fun checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            //Abrir camara
            Toast.makeText(this, "Permisos de ubicacion concedidos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            //Usuario rechazó permisos
            Toast.makeText(
                this,
                "Permisos de ubicacion rechazador por segunda vez",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // pedir permisos
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                777
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 777) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permisos de ubicacion concedidos", Toast.LENGTH_SHORT).show()
            } else {
                //
                Toast.makeText(this, "Permisos rechazdo por primera vez", Toast.LENGTH_SHORT).show()
            }
        }
    }


}