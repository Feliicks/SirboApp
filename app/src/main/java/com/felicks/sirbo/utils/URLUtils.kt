package com.felicks.sirbo.utils

object UrlUtils {

    /**
     * Normaliza la URL para Retrofit:
     * - Asegura que empiece con http:// o https://
     * - Asegura que termine con /
     */
    fun normalizeBaseUrl(url: String): String {
        var tmp = url.trim()

        // Añadir esquema si falta
        if (!tmp.startsWith("http://") && !tmp.startsWith("https://")) {
            tmp = "https://$tmp"
        }

        // Añadir / al final si falta
        if (!tmp.endsWith("/")) {
            tmp += "/"
        }

        return tmp
    }

    /**
     * Valida si la URL tiene un esquema correcto (http o https)
     */
    fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }
}
