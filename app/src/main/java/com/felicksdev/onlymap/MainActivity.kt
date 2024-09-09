package com.felicksdev.onlymap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

class MainActivity : ComponentActivity() {
    // TODO  Validacion de que si el eusautio tiene permisos de ubicacion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme (
//                darkTheme = false
            ) {
                Surface {
                    SirboApp()
                }
            }
        }
    }
}