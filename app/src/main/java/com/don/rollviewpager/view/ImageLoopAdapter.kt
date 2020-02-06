package com.don.rollviewpager.view

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.don.rollviewpager.R
import com.don.rollviewpager.libs.RollPagerView
import com.don.rollviewpager.libs.adapter.LoopPagerAdapter

/**
 * Created by gideon on 06,February,2020
 * gideon@cicil.co.id
 * Jakarta - Indonesia
 */
class ImageLoopAdapter(viewPager: RollPagerView?) :
    LoopPagerAdapter(viewPager!!) {
    var imgs = intArrayOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4,
        R.drawable.img5
    )

    override fun getView(container: ViewGroup?, position: Int): View {
        val view = ImageView(container!!.context)
        view.scaleType = ImageView.ScaleType.CENTER_CROP
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.setImageResource(imgs[position])
        return view
    }

    override val realCount: Int get() = imgs.size
}