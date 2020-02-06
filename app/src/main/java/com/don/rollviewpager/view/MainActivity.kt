package com.don.rollviewpager.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.don.rollviewpager.R


/**
 * Created by zhuchenxi on 2016/12/13.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun simple(view: View?) {
        val i = Intent(this, SimpleActivity::class.java)
        startActivity(i)
    }

    fun loop(view: View?) {
        val i = Intent(this, LoopActivity::class.java)
        startActivity(i)
    }

    fun netImage(view: View?) {
        val i = Intent(this, NetImageActivity::class.java)
        startActivity(i)
    }
}