package com.pay.payomatix.utils

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class CustomScroller(context: Context, interpolator: Interpolator? = null) : Scroller(context, interpolator) {
    private var scrollDuration = 1000 // Default duration: 1 second

    fun setScrollDuration(duration: Int) {
        scrollDuration = duration
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        // Override the default duration
        super.startScroll(startX, startY, dx, dy, scrollDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        // Apply custom duration for all scrolling
        super.startScroll(startX, startY, dx, dy, scrollDuration)
    }
}