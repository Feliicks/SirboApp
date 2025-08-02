package com.felicks.sirbo.ui.utils

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import kotlin.collections.forEach

@Composable
fun rememberMapPadding(bottomPadding: Dp, maxBottomPadding: Dp): PaddingValues {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    return if (isPortrait) {
        rememberPortraitMapPadding(bottomPadding, maxBottomPadding)
    } else {
        remember { PaddingValues() }
    }
}

@Composable
fun rememberPortraitMapPadding(bottomPadding: Dp, maxBottomPadding: Dp): PaddingValues {
    return remember(bottomPadding, maxBottomPadding) {
        PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = bottomPadding.takeIf { it < maxBottomPadding } ?: maxBottomPadding
        )
    }
}

object MapaUtils {
    suspend fun adjustCameraToItineraryList(
        list: List<LatLng>,
        cameraPositionState: CameraPositionState,
        padding: Int = 80
    ) {
        if (list.isEmpty()) return

        try {
            val boundsBuilder = LatLngBounds.Builder()
            list.forEach { boundsBuilder.include(it) }
            val bounds = boundsBuilder.build()

            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bounds, padding)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
