package com.felicks.sirbo.extensions

import androidx.compose.ui.graphics.Color
import com.felicks.sirbo.data.models.SyncStatus

// Define los colores constantes (puedes moverlos a Theme si quieres)
val ColorSincronizando = Color(0xFF4CAF50) // Verde
val ColorSincronizando2 = Color(0xFF03A9F4) // Azul claro

val ColorCargandoLocal = Color(0xFF9E9E9E) // Gris
val ColorError = Color(0xFFFF5722)         // Rojo o naranja

fun SyncStatus.toIndicatorColor(): Color {
    return when (this) {
//        SyncStatus.MOSTRANDO_LOCAL -> ColorCargandoLocal
        SyncStatus.SINCRONIZANDO    -> ColorSincronizando2
        SyncStatus.ERROR_CONEXION,
        SyncStatus.ERROR_INSERCION -> ColorError
        else                       -> Color.Gray
    }
}
