package com.don.rollviewpager.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.don.rollviewpager.R
import com.don.rollviewpager.libs.RollPagerView
import com.don.rollviewpager.libs.adapter.StaticPagerAdapter


class SimpleActivity : AppCompatActivity() {
    private var mViewPager: RollPagerView? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)
        mViewPager = findViewById(R.id.view_pager)
        mViewPager!!.setAdapter(ImageNormalAdapter())
    }

    private inner class ImageNormalAdapter : StaticPagerAdapter() {
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

        override fun getCount(): Int {
            return imgs.size
        }
    }
}