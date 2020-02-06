package com.don.rollviewpager.libs.adapter


import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import java.util.*

/**
 * Created by gideon on 06,February,2020
 * gideon@cicil.co.id
 * Jakarta - Indonesia
 */


/**
 *
 * Subclasses only need to implement [.getView]
 * and [.getCount] to have a working adapter.
 *
 */
abstract class StaticPagerAdapter : PagerAdapter() {
    private val mViewList =
        ArrayList<View>()

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

    override fun notifyDataSetChanged() {
        mViewList.clear()
        super.notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = findViewByPosition(container, position)
        container.addView(itemView)
        onBind(itemView, position)
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

    private fun onBind(view: View?, position: Int) {}
    abstract fun getView(container: ViewGroup?, position: Int): View
}