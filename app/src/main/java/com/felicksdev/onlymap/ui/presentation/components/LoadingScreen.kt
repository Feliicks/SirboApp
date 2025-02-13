package com.felicksdev.onlymap.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun LoadingScreen() {
    // Usamos un Box para apilar la pantalla de carga sobre el contenido
    Box(modifier = Modifier.fillMaxSize()) {
        // El contenido principal de la pantalla (Mapa, etc.)

        Box(
            modifier = Modifier
                .fillMaxSize() // Cubrimos toda la pantalla
                .background(Color.Black.copy(alpha = 0.5f)) // Fondo semi-transparente
                .zIndex(1f), // Asegura que esté sobre el contenido
            contentAlignment = Alignment.Center // Centra el indicador de carga
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp), // Tamaño del indicador de carga
                color = Color.White, // Color del indicador de carga
                strokeWidth = 4.dp // Grosor del indicador de carga
            )
        }
    }

}
