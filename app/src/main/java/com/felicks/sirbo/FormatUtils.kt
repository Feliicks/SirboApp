package com.felicks.sirbo

import kotlin.math.roundToInt

// 🔹 Convierte una duración en segundos a un formato legible (minutos / horas)
fun formatDuration(seconds: Double): String {
    val totalMinutes = (seconds / 60.0).toInt()
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60

    return when {
        totalMinutes < 2 -> "< 2 min" // ✅ Evita "0 min" o "1 min"
        totalMinutes in 58..59 -> "casi 1 h" // ✅ Si está cerca de 1 hora
        hours > 0 && minutes in 58..59 -> "casi ${hours + 1} h" // ✅ "casi 2 h" si es 1 h 58 min
        hours > 0 && minutes > 2 -> "~ $hours h $minutes min" // ✅ Usa "~" si hay minutos extra
        hours > 0 -> "$hours h" // ✅ Si es una hora exacta, no usa "~"
        else -> "~ $minutes min" // ✅ En minutos, usa "~" si es inexacto
    }
}

// 🔹 Convierte una distancia en metros a un formato legible (metros / kilómetros)
fun formatDistance(distanceMeters: Double): String {
    return if (distanceMeters < 1000) {
        "${distanceMeters.roundToInt()} m" // ✅ Menos de 1 km → Mostrar en metros
    } else {
        "${"%.1f".format(distanceMeters / 1000)} km" // ✅ 1 km o más → Mostrar en km con 1 decimal
    }
}

// 🔹 Formatea un nombre de lugar (por ejemplo, elimina "Unnamed Road" si está presente)
fun formatPlaceName(name: String?): String {
    return name?.takeIf { it.isNotBlank() } ?: "Ubicación desconocida"
}

