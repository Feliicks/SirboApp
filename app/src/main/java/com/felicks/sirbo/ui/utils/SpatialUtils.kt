package com.felicks.sirbo.ui.utils


import android.util.Log
import com.felicks.sirbo.data.remote.photon.PhotonFeature
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

object SpatialUtils {

    private const val TAG = "SpatialUtils"

    private fun distanciaEnMetros(c1: List<Double>, c2: List<Double>): Double {
        val R = 6371e3 // Radio de la Tierra en metros
        val lat1 = Math.toRadians(c1[1])
        val lat2 = Math.toRadians(c2[1])
        val deltaLat = Math.toRadians(c2[1] - c1[1])
        val deltaLng = Math.toRadians(c2[0] - c1[0])

        val a = sin(deltaLat / 2).pow(2) +
                cos(lat1) * cos(lat2) * sin(deltaLng / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    fun deduplicarFeaturesPorNombreYDistancia(
        features: List<PhotonFeature>,
        maxDistanceMeters: Double = 50.0,
        verbose: Boolean = false
    ): List<PhotonFeature> {

        val deduplicados = mutableListOf<PhotonFeature>()

        for ((index, actual) in features.withIndex()) {
            val nombre = actual.properties.name?.trim()?.lowercase() ?: continue
            val ciudad = actual.properties.city?.trim()?.lowercase()
                ?: actual.properties.state?.trim()?.lowercase()
                ?: actual.properties.country?.trim()?.lowercase()
                ?: continue

            val yaExiste = deduplicados.any { ya ->
                val mismoNombre = ya.properties.name?.trim()?.lowercase() == nombre
                val mismaCiudad = (ya.properties.city?.trim()?.lowercase()
                    ?: ya.properties.state?.trim()?.lowercase()
                    ?: ya.properties.country?.trim()?.lowercase()) == ciudad

                val dist = distanciaEnMetros(ya.geometry.coordinates, actual.geometry.coordinates)

                val esDuplicado = mismoNombre && mismaCiudad && dist < maxDistanceMeters

                if (verbose && esDuplicado) {
                    Log.d(TAG, "🟡 Duplicado: \"$nombre\" en \"$ciudad\" a ${dist.toInt()}m del otro.")
                }

                esDuplicado
            }

            if (!yaExiste) {
                if (verbose) {
                    Log.d(TAG, "✅ Añadido: \"$nombre\" en \"$ciudad\" (índice $index)")
                }
                deduplicados.add(actual)
            }
        }

        if (verbose) {
            Log.d(TAG, "🔢 Total original: ${features.size}, deduplicados: ${deduplicados.size}")
        }

        return deduplicados
    }
}
