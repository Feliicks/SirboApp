package com.felicks.sirbo.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.felicks.sirbo.ui.theme.BusColor
import com.felicks.sirbo.ui.theme.DefaultColor
import com.felicks.sirbo.ui.theme.TaxiColor
import com.felicks.sirbo.ui.theme.WalkColor

// 📌 TransportMode SIN definición de color directa
enum class TransportMode(val icon: ImageVector) {
    WALK(Icons.Filled.DirectionsWalk),  // Caminar
    BUS(Icons.Filled.DirectionsBus),    // Bus
    BIKE(Icons.Filled.DirectionsBike),  // Bicicleta
    TAXI(Icons.Filled.DirectionsBus),   // Taxi (Placeholder)
    UNKNOWN(Icons.Filled.DirectionsBus); // Default (Gris)

    companion object {
        fun fromMode(mode: String): TransportMode {
            return values().find { it.name.equals(mode, ignoreCase = true) } ?: UNKNOWN
        }
    }
}

// 📌 Función de extensión para asignar colores desde `getColor()`
fun TransportMode.getColor(): Color {
    return when (this) {
        TransportMode.BUS -> BusColor
        TransportMode.WALK -> WalkColor
//        TransportMode.METRO -> MetroColor
        TransportMode.TAXI -> TaxiColor
        else -> DefaultColor // Si no se reconoce, usar gris
    }
}