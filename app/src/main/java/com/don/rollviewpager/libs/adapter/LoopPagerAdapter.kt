package com.don.rollviewpager.libs.adapter

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.don.rollviewpager.libs.RollPagerView
import com.don.rollviewpager.libs.RollPagerView.HintViewDelegate
import com.don.rollviewpager.libs.hintview.HintView
import java.util.*

/**
 * Created by gideon on 06,February,2020
 * gideon@cicil.co.id
 * Jakarta - Indonesia
 */

abstract class LoopPagerAdapter(private val mViewPager: RollPagerView) : PagerAdapter() {
    private val mViewList =
        ArrayList<View>()

    private inner class LoopHintViewDelegate : HintViewDelegate {
        override fun setCurrentPosition(position: Int, hintView: HintView?) {
            if (hintView != null && realCount > 0) hintView.setCurrent(position % realCount)
        }

        override fun initView(length: Int, mGravity: Int, hintView: HintView?) {
            hintView?.initView(realCount, mGravity)
        }
    }

    override fun notifyDataSetChanged() {
        mViewList.clear()
        initPosition()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        super.registerDataSetObserver(observer)
        initPosition()
    }

    private fun initPosition() {
        if (mViewPager.viewPager!!.currentItem == 0 && realCount > 0) {
            val half = Int.MAX_VALUE / 2
            val start = half - half % realCount
            setCurrent(start)
        }
    }

    private fun setCurrent(index: Int) {
        try {
            val field = ViewPager::class.java.getDeclaredField("mCurItem")
            field.isAccessible = true
            field[mViewPager.viewPager] = index
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

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

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val realPosition = position % realCount
        val itemView = findViewByPosition(container, realPosition)
        container.addView(itemView)
        return itemView
    }

    private fun findViewByPosition(container: ViewGroup, position: Int): View {
        for (view in mViewList) {
            if (view.tag as Int == position && view.parent == null) {
                return view
            }
        }
        val view = getView(container, position)
        view.tag = position
        mViewList.add(view)
        return view
    }

    abstract fun getView(container: ViewGroup?, position: Int): View

    override fun getCount(): Int {
        return if (realCount <= 0) realCount else Int.MAX_VALUE
    }

    abstract val realCount: Int

    init {
        mViewPager.setHintViewDelegate(LoopHintViewDelegate())
    }
}