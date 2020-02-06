package com.don.rollviewpager.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.don.rollviewpager.R
import com.don.rollviewpager.libs.RollPagerView
import com.don.rollviewpager.libs.adapter.LoopPagerAdapter
import com.don.rollviewpager.libs.hintview.TextHintView


/**
 * Created by zhuchenxi on 2016/12/13.
 */
class LoopActivity : AppCompatActivity() {
    private lateinit var mViewPager: RollPagerView
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop)
        mViewPager = findViewById(R.id.view_pager)
        mViewPager.setAdapter(ImageLoopAdapter(mViewPager))
        mViewPager.setHintView(TextHintView(this))
//        mViewPager.setHintView(IconHintView(this, R.drawable.point_focus, R.drawable.point_normal))
//        mViewPager.setHintView(ColorPointHintView(this, Color.YELLOW, Color.WHITE))
    }


}