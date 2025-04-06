package com.felicks.sirbo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // TODO  Validacion de que si el usuario tiene permisos de ubicacion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}


@Composable
fun App() {
    // Alterna entre temas claro y oscuro dinámicamente
//    val isDarkTheme = isSystemInDarkTheme() // Detecta el tema del sistema (opcional)
    val isDarkTheme = false // Detecta el tema del sistema (opcional)

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme(),
    ) {
        // Superficie principal de la aplicación
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SirboApp() // Contenido principal de tu aplicación
        }
    }
}


