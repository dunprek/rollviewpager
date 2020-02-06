package com.don.rollviewpager.libs.hintview

/**
 * Created by gideon on 06,February,2020
 * gideon@cicil.co.id
 * Jakarta - Indonesia
 */
interface HintView {
    fun initView(length: Int, gravity: Int)
    fun setCurrent(current: Int)
}