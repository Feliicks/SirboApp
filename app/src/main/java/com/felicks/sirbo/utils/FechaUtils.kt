package com.felicks.sirbo.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object FechaUtils {

    fun dateToString(date: Date ): String {
        val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        formato.timeZone = TimeZone.getDefault()
        return formato.format(date)
    }

    fun stringToDate(fechaIso: String): Date? {
        return try {
            val formato = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            formato.timeZone = TimeZone.getDefault()
            formato.parse(fechaIso)
        } catch (e: Exception) {
            null
        }
    }
}