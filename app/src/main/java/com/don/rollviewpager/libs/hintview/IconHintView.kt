package com.don.rollviewpager.libs.hintview


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.don.rollviewpager.Util

/**
 * Created by gideon on 06,February,2020
 * gideon@cicil.co.id
 * Jakarta - Indonesia
 */

class IconHintView constructor(
    context: Context, @param:DrawableRes private val focusResId: Int, @param:DrawableRes private val normalResId: Int,
    private val size: Int = Util.dip2px(context, 32f)
) :
    ShapeHintView(context) {
    override fun makeFocusDrawable(): Drawable? {
        var drawable =
            context.resources.getDrawable(focusResId)
        if (size > 0) {
            drawable = zoomDrawable(drawable, size, size)
        }
        return drawable
    }

    override fun makeNormalDrawable(): Drawable? {
        var drawable =
            context.resources.getDrawable(normalResId)
        if (size > 0) {
            drawable = zoomDrawable(drawable, size, size)
        }
        return drawable
    }

    private fun zoomDrawable(
        drawable: Drawable,
        w: Int,
        h: Int
    ): Drawable {
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        val oldbmp = drawableToBitmap(drawable)
        val matrix = Matrix()
        val scaleWidth = w.toFloat() / width
        val scaleHeight = h.toFloat() / height
        matrix.postScale(scaleWidth, scaleHeight)
        val newbmp = Bitmap.createBitmap(
            oldbmp, 0, 0, width, height,
            matrix, true
        )
        return BitmapDrawable(null, newbmp)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        val config =
            if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        val bitmap = Bitmap.createBitmap(width, height, config)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return bitmap
    }

}