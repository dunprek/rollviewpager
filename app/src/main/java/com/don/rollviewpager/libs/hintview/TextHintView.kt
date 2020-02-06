package com.don.rollviewpager.libs.hintview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.don.rollviewpager.R
import java.text.MessageFormat


/**
 * Created by gideon on 06,February,2020
 * gideon@cicil.co.id
 * Jakarta - Indonesia
 */
class TextHintView : TextView, HintView {
    private var length = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun initView(length: Int, gravity: Int) {
        this.length = length

        setTextColor(Color.WHITE)
        setBackgroundResource(R.drawable.drawable_box_text_tranparent)
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        this.setMargins(16,0,0,16)
        setPadding(8, 1, 8, 1)


        when (gravity) {
            0 -> setGravity(Gravity.LEFT or Gravity.CENTER_VERTICAL)
            1 -> setGravity(Gravity.CENTER)
            2 -> setGravity(Gravity.RIGHT or Gravity.CENTER_VERTICAL)
        }
        setCurrent(0)
    }

    override fun setCurrent(current: Int) {
        text = MessageFormat.format("{0}/{1}", current + 1, length)
    }

    private fun View.setMargins(
        leftMarginDp: Int? = null,
        topMarginDp: Int? = null,
        rightMarginDp: Int? = null,
        bottomMarginDp: Int? = null
    ) {
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            val params = layoutParams as ViewGroup.MarginLayoutParams
            leftMarginDp?.run { params.leftMargin = this.dpToPx(context) }
            topMarginDp?.run { params.topMargin = this.dpToPx(context) }
            rightMarginDp?.run { params.rightMargin = this.dpToPx(context) }
            bottomMarginDp?.run { params.bottomMargin = this.dpToPx(context) }
            requestLayout()
        }
    }

    private fun Int.dpToPx(context: Context): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
    }

}