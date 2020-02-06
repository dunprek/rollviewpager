package com.don.rollviewpager

import android.content.Context

/**
 * Created by gideon on 06,February,2020
 * gideon@cicil.co.id
 * Jakarta - Indonesia
 */
object Util {
    /**
     * dpתpx
     *
     */
     fun dip2px(ctx: Context, dpValue: Float): Int {
        val scale: Float = ctx.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * pxתdp
     */
    fun px2dip(ctx: Context, pxValue: Float): Int {
        val scale = ctx.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}