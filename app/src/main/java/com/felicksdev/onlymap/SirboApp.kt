package com.felicksdev.onlymap

import android.Manifest
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.felicksdev.onlymap.ui.navigation.NavigationHost
import com.felicksdev.onlymap.ui.presentation.components.bottomBars.BottomNavigationBar
import com.felicksdev.onlymap.ui.theme.OnlyMapTheme
import com.felicksdev.onlymap.utils.MapConfig
import com.felicksdev.onlymap.viewmodel.HomeScreenViewModel
import com.felicksdev.onlymap.viewmodel.LocationViewModel
import com.felicksdev.onlymap.viewmodel.PlannerViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SirboApp() {
    val plannerViewModel: PlannerViewModel = hiltViewModel()
    val locationViewModel: LocationViewModel = hiltViewModel()
//    Se puede eliminar sintacia usano DI

    val navController = rememberNavController()
    val homeScreenViewModel = HomeScreenViewModel()
    val isPlacesDefined by plannerViewModel.isLocationDefined.collectAsState()

    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Estado mutable para actualizar MapConfig dinámicamente
    var mapProperties by remember { mutableStateOf(MapConfig.mapProperties) }
    var mapUiSetting by remember { mutableStateOf(MapConfig.mapUiConfig) }

    LaunchedEffect(Unit) {
        if (permissionState.status is PermissionStatus.Denied) {
            permissionState.launchPermissionRequest()
        }
    }

// Verifica si el permiso es concedido y actualiza isMyLocationEnabled
    LaunchedEffect(permissionState.status) {
        if (permissionState.status is PermissionStatus.Granted) {
            mapProperties = mapProperties.copy(isMyLocationEnabled = true)
            mapUiSetting = mapUiSetting.copy(myLocationButtonEnabled = true)

            // 🚀 También actualiza `MapConfig` para reflejar los cambios globalmente
            MapConfig.mapProperties = mapProperties
            MapConfig.mapUiConfig = mapUiSetting
        }
    }

    Log.d("SirboApp", "isMyLocationEnabled: ${MapConfig.mapUiConfig}")
    Log.d("SirboApp", "isMyLocationEnabled: ${MapConfig.mapProperties}")
    OnlyMapTheme {
        Scaffold(bottomBar = {
            if (isPlacesDefined) {
                Log.d("SirboApp", "$isPlacesDefined Se muestra el bottom sheet")
                CustomBottomSheet()
            } else {
                Log.d("SirboApp", " $isPlacesDefined Se muestra el navigation bar")
                BottomNavigationBar(navController = navController)
            }
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
