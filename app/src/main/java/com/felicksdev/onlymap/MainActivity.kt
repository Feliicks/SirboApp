package com.felicksdev.onlymap

import RoutesViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // TODO  Validacion de que si el eusautio tiene permisos de ubicacion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val permissionStatus = rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)
//            permissionStatus.launchPermissionRequest()

            MaterialTheme(
//                darkTheme = false
            ) {
                Surface {
                    SirboApp()
                }
            }
        }
    }
}