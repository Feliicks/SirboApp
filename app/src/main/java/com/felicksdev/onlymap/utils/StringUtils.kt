package com.felicksdev.onlymap.utils

object StringUtils {

    fun extractRouteType(longName: String): String {
        val parts = longName.split(Regex("[:\\-]"))
        return if (parts.size > 1) parts[0] else longName
    }

    fun extractRouteDirection(longName: String): String {
        val parts = longName.split(Regex("[:\\-]"))
        return if (parts.size > 1) parts[1] else longName
    }
}