package com.don.rollviewpager.libs.hintview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout


/**
 * Created by gideon on 06,February,2020
 * gideon@cicil.co.id
 * Jakarta - Indonesia
 */
abstract class ShapeHintView : LinearLayout, HintView {
    private lateinit var mDots: Array<ImageView?>
    private var length = 0
    private var lastPosition = 0
    private var dotNormal: Drawable? = null
    private var dotFocus: Drawable? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    abstract fun makeFocusDrawable(): Drawable?
    abstract fun makeNormalDrawable(): Drawable?
    override fun initView(length: Int, gravity: Int) {
        removeAllViews()
        lastPosition = 0
        orientation = HORIZONTAL
        when (gravity) {
            0 -> setGravity(Gravity.LEFT or Gravity.CENTER_VERTICAL)
            1 -> setGravity(Gravity.CENTER)
            2 -> setGravity(Gravity.RIGHT or Gravity.CENTER_VERTICAL)
        }
        this.length = length
        mDots = arrayOfNulls(length)
        dotFocus = makeFocusDrawable()
        dotNormal = makeNormalDrawable()
        for (i in 0 until length) {
            mDots[i] = ImageView(context)
            val dotlp = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            dotlp.setMargins(10, 0, 10, 0)
            mDots[i]!!.layoutParams = dotlp
            mDots[i]!!.setBackgroundDrawable(dotNormal)
            addView(mDots[i])
        }
        setCurrent(0)
    }

    override fun setCurrent(current: Int) {
        if (current < 0 || current > length - 1) {
            return
        }
        mDots[lastPosition]!!.setBackgroundDrawable(dotNormal)
        mDots[current]!!.setBackgroundDrawable(dotFocus)
        lastPosition = current
    }
}
