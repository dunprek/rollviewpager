package com.don.rollviewpager.libs

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.RelativeLayout
import android.widget.Scroller
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.don.rollviewpager.R
import com.don.rollviewpager.Util
import com.don.rollviewpager.libs.adapter.LoopPagerAdapter
import com.don.rollviewpager.libs.hintview.HintView
import java.lang.ref.WeakReference
import java.util.*


/**
 * 支持轮播和提示的的viewpager
 */

class RollPagerView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    RelativeLayout(context, attrs, defStyle), OnPageChangeListener {
    /**
     * 取真正的Viewpager
     * @return
     */
    var viewPager: ViewPager? = null
    private var mAdapter: PagerAdapter? = null
    private var mOnItemClickListener: OnItemViewClickListener? = null
    private var mGestureDetector: GestureDetector? = null
    private var mRecentTouchTime: Long = 0
    //播放延迟
    private var delay = 0
    //hint位置
    private var mGravity = 0
    //hint颜色
    private var mColor = 0
    //hint透明度
    private var mAlpha = 0
    private var mPaddingLeft = 0
    private var mPaddingTop = 0
    private var mPaddingRight = 0
    private var mPaddingBottom = 0
    private var mHintView: View? = null
    private var timer: Timer? = null

    interface HintViewDelegate {
        fun setCurrentPosition(position: Int, hintView: HintView?)
        fun initView(length: Int, mGravity: Int, hintView: HintView?)
    }

    private var mHintViewDelegate: HintViewDelegate =
        object : HintViewDelegate {
            override fun setCurrentPosition(
                position: Int,
                hintView: HintView?
            ) {
                hintView?.setCurrent(position)
            }

            override fun initView(
                length: Int,
                mGravity: Int,
                hintView: HintView?
            ) {
                hintView?.initView(length, mGravity)
            }
        }

    /**
     * 读取提示形式  和   提示位置   和    播放延迟
     * @param attrs
     */
    private fun initView(attrs: AttributeSet?) {
        if (viewPager != null) {
            removeView(viewPager)
        }
        val type = context.obtainStyledAttributes(attrs, R.styleable.RollViewPager)
        mGravity = type.getInteger(R.styleable.RollViewPager_rollviewpager_hint_gravity, 1)
        delay = type.getInt(R.styleable.RollViewPager_rollviewpager_play_delay, 0)
        mColor = type.getColor(
            R.styleable.RollViewPager_rollviewpager_hint_color,
            Color.BLACK
        )
        mAlpha = type.getInt(R.styleable.RollViewPager_rollviewpager_hint_alpha, 0)
        mPaddingLeft =
            type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingLeft, 0f).toInt()
        mPaddingRight =
            type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingRight, 0f).toInt()
        mPaddingTop =
            type.getDimension(R.styleable.RollViewPager_rollviewpager_hint_paddingTop, 0f).toInt()
        mPaddingBottom = type.getDimension(
            R.styleable.RollViewPager_rollviewpager_hint_paddingBottom,
            Util.dip2px(context, 4f).toFloat()
        ).toInt()
        viewPager = ViewPager(context)
        viewPager!!.id = R.id.viewpager_inner
        viewPager!!.layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        addView(viewPager)
        type.recycle()

        //手势处理
        mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                if (mOnItemClickListener != null) {
                    if (mAdapter is LoopPagerAdapter) { //原谅我写了这么丑的代码
                        mOnItemClickListener!!.onItemViewClick(viewPager!!.currentItem % (mAdapter as LoopPagerAdapter).realCount)
                    } else {
                        mOnItemClickListener!!.onItemViewClick(viewPager!!.currentItem)
                    }
                }
                return super.onSingleTapUp(e)
            }
        })
    }

    private class TimeTaskHandler(rollPagerView: RollPagerView) : Handler() {
        private val mRollPagerViewWeakReference: WeakReference<RollPagerView> = WeakReference(rollPagerView)
        override fun handleMessage(msg: Message) {
            val rollPagerView = mRollPagerViewWeakReference.get()
            var cur = rollPagerView!!.viewPager!!.currentItem + 1
            if (cur >= rollPagerView.mAdapter!!.count) {
                cur = 0
            }
            rollPagerView.viewPager!!.currentItem = cur
            rollPagerView.mHintViewDelegate.setCurrentPosition(
                cur,
                rollPagerView.mHintView as HintView?
            )
            if (rollPagerView.mAdapter!!.count <= 1) rollPagerView.stopPlay()
        }

    }

    private val mHandler = TimeTaskHandler(this)

    private class WeakTimerTask(mRollPagerView: RollPagerView) : TimerTask() {
        private val mRollPagerViewWeakReference: WeakReference<RollPagerView> = WeakReference(mRollPagerView)
        override fun run() {
            val rollPagerView = mRollPagerViewWeakReference.get()
            if (rollPagerView != null) {
                if (rollPagerView.isShown && System.currentTimeMillis() - rollPagerView.mRecentTouchTime > rollPagerView.delay) {
                    rollPagerView.mHandler.sendEmptyMessage(0)
                }
            } else {
                cancel()
            }
        }

    }

    /**
     * 开始播放
     * 仅当view正在显示 且 触摸等待时间过后 播放
     */
    private fun startPlay() {
        if (delay <= 0 || mAdapter == null || mAdapter!!.count <= 1) {
            return
        }
        if (timer != null) {
            timer!!.cancel()
        }
        timer = Timer()
        //用一个timer定时设置当前项为下一项
        timer!!.schedule(WeakTimerTask(this), delay.toLong(), delay.toLong())
    }

    private fun stopPlay() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    fun setHintViewDelegate(delegate: HintViewDelegate) {
        mHintViewDelegate = delegate
    }

    private fun initHint(hintview: HintView?) {
        if (mHintView != null) {
            removeView(mHintView)
        }
        if (hintview == null) {
            return
        }
        mHintView = hintview as View?
        loadHintView()
    }

    /**
     * 加载hintview的容器
     */
    private fun loadHintView() {
        addView(mHintView)
        mHintView!!.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
        val lp = LayoutParams(
            LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.addRule(ALIGN_PARENT_BOTTOM)
        mHintView!!.layoutParams = lp
        val gd = GradientDrawable()
        gd.setColor(mColor)
        gd.alpha = mAlpha
        mHintView!!.setBackgroundDrawable(gd)
        mHintViewDelegate.initView(
            if (mAdapter == null) 0 else mAdapter!!.count,
            gravity,
            mHintView as HintView?
        )
    }

    /**
     * 设置viewager滑动动画持续时间
     * @param during
     */
    fun setAnimationDurtion(during: Int) {
        try { // viePager平移动画事件
            val mField =
                ViewPager::class.java.getDeclaredField("mScroller")
            mField.isAccessible = true
            val mScroller: Scroller = object : Scroller(
                context,  // 动画效果与ViewPager的一致
                Interpolator { t ->
                    var t = t
                    t -= 1.0f
                    t * t * t * t * t + 1.0f
                }) {
                override fun startScroll(
                    startX: Int, startY: Int, dx: Int,
                    dy: Int, duration: Int
                ) { // 如果手工滚动,则加速滚动
                    var duration = duration
                    if (System.currentTimeMillis() - mRecentTouchTime > delay) {
                        duration = during
                    } else {
                        duration /= 2
                    }
                    super.startScroll(startX, startY, dx, dy, duration)
                }

                override fun startScroll(
                    startX: Int, startY: Int, dx: Int,
                    dy: Int
                ) {
                    super.startScroll(startX, startY, dx, dy, during)
                }
            }
            mField[viewPager] = mScroller
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    fun setPlayDelay(delay: Int) {
        this.delay = delay
        startPlay()
    }

    fun pause() {
        stopPlay()
    }

    fun resume() {
        startPlay()
    }

    val isPlaying: Boolean
        get() = timer != null

    fun setOnItemClickListener(listener: OnItemViewClickListener?) {
        mOnItemClickListener = listener
    }

    /**
     * 设置提示view的位置
     *
     */
    fun setHintPadding(left: Int, top: Int, right: Int, bottom: Int) {
        mPaddingLeft = left
        mPaddingTop = top
        mPaddingRight = right
        mPaddingBottom = bottom
        mHintView!!.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
    }

    /**
     * 设置提示view的透明度
     * @param alpha 0为全透明  255为实心
     */
    fun setHintAlpha(mAlpha: Int) {
        this.mAlpha = mAlpha
        initHint(mHintView as HintView?)
    }

    /**
     * 支持自定义hintview
     * 只需new一个实现HintView的View传进来
     * 会自动将你的view添加到本View里面。重新设置LayoutParams。
     * @param hintview
     */
    fun setHintView(hintview: HintView?) {
        if (mHintView != null) {
            removeView(mHintView)
        }
        mHintView = hintview as View?
        if (hintview != null) {
            initHint(hintview)
        }
    }

    /**
     * 设置Adapter
     * @param adapter
     */
    fun setAdapter(adapter: PagerAdapter) {
        adapter.registerDataSetObserver(JPagerObserver())
        viewPager!!.adapter = adapter
        viewPager!!.addOnPageChangeListener(this)
        mAdapter = adapter
        dataSetChanged()
    }

    /**
     * 用来实现adapter的notifyDataSetChanged通知HintView变化
     */
    private inner class JPagerObserver : DataSetObserver() {
        override fun onChanged() {
            dataSetChanged()
        }

        override fun onInvalidated() {
            dataSetChanged()
        }
    }

    private fun dataSetChanged() {
        if (mHintView != null) {
            mHintViewDelegate.initView(mAdapter!!.count, gravity, mHintView as HintView?)
            mHintViewDelegate.setCurrentPosition(
                viewPager!!.currentItem,
                mHintView as HintView?
            )
        }
        startPlay()
    }

    /**
     * 为了实现触摸时和过后一定时间内不滑动,这里拦截
     * @param ev
     * @return
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        mRecentTouchTime = System.currentTimeMillis()
        mGestureDetector!!.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    override fun onPageScrollStateChanged(arg0: Int) {}

    override fun onPageScrolled(
        arg0: Int,
        arg1: Float,
        arg2: Int
    ) {}

    override fun onPageSelected(arg0: Int) {
        mHintViewDelegate.setCurrentPosition(arg0, mHintView as HintView?)
    }

    init {
        initView(attrs)
    }
}