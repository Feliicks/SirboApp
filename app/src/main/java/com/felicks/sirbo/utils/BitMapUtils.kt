package com.felicks.sirbo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object BitMapUtils {
    @Deprecated("Use vectorDrawableToBitmapDescriptor instead")
    fun bitmapDescriptor(
        context: Context,
        vectorResId: Int
    ): BitmapDescriptor? {

        // retrieve the actual drawable
        val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        val bm = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // draw it onto the bitmap
        val canvas = android.graphics.Canvas(bm)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }

    fun vectorDrawableToBitmap(context: Context, drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableId) ?: return null
        return drawable.toBitmap()
    }

    fun vectorDrawableToBitmapDescriptor(
        context: Context,
        @DrawableRes drawableId: Int,
        sizeDp: Int = 48
    ): BitmapDescriptor? {
        val drawable = ContextCompat.getDrawable(context, drawableId) ?: return null
        val scale = context.resources.displayMetrics.density // 📏 Convertir `dp` a `px`
        val sizePx = (sizeDp * scale).toInt()

        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

}