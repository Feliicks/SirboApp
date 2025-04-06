package com.felicks.sirbo.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
/**
 * Calcula el grosor de la polilínea en función del nivel de zoom.
 */
@Composable
fun rememberScaledWidth(zoom: Float): Float {
    val baseWidth = 5f
    val zoomFactor = 10f
    val minWidth = 13f
    val maxWidth = 23f
    return remember(zoom) { (baseWidth * (zoom / zoomFactor)).coerceIn(minWidth, maxWidth) }
}