package com.pay.payomatix.utils

import androidx.viewpager.widget.ViewPager

object Utils {

    fun setCustomScroller(viewPager: ViewPager) {
        try {
            // Access ViewPager's Scroller field
            val viewPagerClass = ViewPager::class.java
            val scrollerField = viewPagerClass.getDeclaredField("mScroller")
            scrollerField.isAccessible = true

            // Set the custom scroller
            val customScroller = CustomScroller(viewPager.context)
            customScroller.setScrollDuration(800)
            scrollerField.set(viewPager, customScroller)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}