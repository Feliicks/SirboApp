package com.felicks.sirbo.utils

import com.felicks.sirbo.data.models.TransportMode

object StringUtils {

    fun extractRouteType(longName: String): String {
        val parts = longName.split(Regex("[:\\-]"))
        return if (parts.size > 1) parts[0] else longName
    }

    fun extractRouteDirection(longName: String): String {
        val parts = longName.split(Regex("[:\\-]"))
        return if (parts.size > 1) parts[1] else longName
    }

    fun String.toTransportMode(): TransportMode {
        return try {
            TransportMode.valueOf(this.uppercase()) // Convierte "bus" -> "BUS"
        } catch (e: IllegalArgumentException) {
            TransportMode.UNKNOWN // Si no existe, usa un valor por defecto
        }
    }
}