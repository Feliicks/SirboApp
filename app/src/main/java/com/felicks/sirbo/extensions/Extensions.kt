package com.felicks.sirbo.extensions

import androidx.compose.ui.graphics.Color
import com.felicks.sirbo.data.models.SyncStatus

// Define los colores constantes (puedes moverlos a Theme si quieres)
val ColorSincronizando = Color(0xFF4CAF50) // Verde
val ColorSincronizando2 = Color(0xFF03A9F4) // Azul claro
val ColorError = Color(0xFFFA3939) // Azul claro

val ColorCargandoLocal = Color(0xFF9E9E9E) // Gris
val ColorWarn = Color(0xFFFFBF6F)         // Rojo o naranja

fun SyncStatus.toIndicatorColor(): Color {
    return when (this) {
//        SyncStatus.MOSTRANDO_LOCAL -> ColorCargandoLocal
        SyncStatus.SINCRONIZANDO -> ColorSincronizando2
        SyncStatus.SINCRONIZADO -> ColorSincronizando2
        SyncStatus.ERROR_GENERAL -> ColorError
        SyncStatus.ERROR_CONEXION -> ColorWarn
        SyncStatus.ERROR_INSERCION -> ColorError

        else
            -> ColorSincronizando2
    }
}
