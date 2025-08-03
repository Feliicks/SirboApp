package com.felicks.sirbo.extensions

import androidx.compose.ui.graphics.Color
import com.felicks.sirbo.domain.models.SyncStatus

// Define los colores constantes (puedes moverlos a Theme si quieres)
// Colores representativos para cada estado de sincronización
val ColorSincronizando = Color(0xFF4CAF50)   // Verde
val ColorSincronizando2 = Color(0xFF03A9F4)  // Azul claro
val ColorError = Color(0xFFFA3939)           // Rojo
val ColorCargandoLocal = Color(0xFF9E9E9E)   // Gris
val ColorWarn = Color(0xFFFFC107)            // Naranja
val ColorVacio = Color(0xFFBDBDBD)           // Gris claro
val ColorCompleto = Color(0xFF4CAF50)        // Verde (igual que sincronizado)

// Extensión que devuelve el color según el estado
fun SyncStatus.toIndicatorColor(): Color {
    return when (this) {
        SyncStatus.MOSTRANDO_LOCAL -> ColorCargandoLocal
        SyncStatus.SINCRONIZANDO   -> ColorSincronizando2
        SyncStatus.ERROR_INSERCION -> ColorError
        SyncStatus.ERROR_CONEXION  -> ColorWarn
        SyncStatus.VACIO_REMOTO    -> ColorVacio
        SyncStatus.COMPLETADO      -> ColorCompleto
        SyncStatus.ERROR_GENERAL   -> ColorError
        SyncStatus.SINCRONIZADO    -> ColorSincronizando
    }
}

