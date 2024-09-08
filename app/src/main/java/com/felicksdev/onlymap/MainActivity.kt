package com.felicksdev.onlymap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.felicksdev.onlymap.ui.theme.OnlyMapTheme

class MainActivity : ComponentActivity() {
    // TODO  Validacion de que si el eusautio tiene permisos de ubicacion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlyMapTheme (
                darkTheme = false
            ) {
                Surface {
                    SirboApp()
                }
            }
        }
    }
}