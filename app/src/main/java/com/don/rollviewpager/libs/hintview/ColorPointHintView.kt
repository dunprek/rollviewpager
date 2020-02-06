package com.don.rollviewpager.libs.hintview

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import com.don.rollviewpager.Util.dip2px

/**
 * Created by gideon on 06,February,2020
 * gideon@cicil.co.id
 * Jakarta - Indonesia
 */

class ColorPointHintView(
    context: Context?,
    private val focusColor: Int,
    private val normalColor: Int
) : ShapeHintView(context) {
    override fun makeFocusDrawable(): Drawable? {
        val dotFocus = GradientDrawable()
        dotFocus.setColor(focusColor)
        dotFocus.cornerRadius = dip2px(context, 4f).toFloat()
        dotFocus.setSize(
            dip2px(context, 8f),
            dip2px(context, 8f)
        )
        return dotFocus
    }

    override fun makeNormalDrawable(): Drawable? {
        val dotNormal = GradientDrawable()
        dotNormal.setColor(normalColor)
        dotNormal.cornerRadius = dip2px(context, 4f).toFloat()
        dotNormal.setSize(
            dip2px(context, 8f),
            dip2px(context, 8f)
        )
        return dotNormal
    }

}