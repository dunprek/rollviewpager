package com.don.rollviewpager.libs.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by gideon on 06,February,2020
 * gideon@cicil.co.id
 * Jakarta - Indonesia
 */


abstract class DynamicPagerAdapter : PagerAdapter() {
    override fun isViewFromObject(
        arg0: View,
        arg1: Any
    ): Boolean {
        return arg0 === arg1
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = getView(container, position)
        container.addView(itemView)
        return itemView
    }

    abstract fun getView(container: ViewGroup?, position: Int): View
}