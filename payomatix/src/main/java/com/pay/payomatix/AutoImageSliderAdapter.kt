package com.pay.payomatix

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class AutoImageSliderAdapter(private val context: Context, private val imageList: List<Int>) : PagerAdapter() {

    private var currentPage = 0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun getCount(): Int {
        // Return a large number to simulate infinite scrolling
        return Int.MAX_VALUE
    }

    private fun getActualPosition(position: Int): Int {
        // Calculate the actual position within the image list, considering infinite scrolling
        return position % imageList.size
    }

    fun autoSlide(viewPager: ViewPager) {
        handler = Handler(Looper.getMainLooper())

        runnable = object : Runnable {
            override fun run() {
                // Increment the current page and update the ViewPager
                currentPage++
                viewPager.setCurrentItem(currentPage, true)

                // Post the runnable again for continuous scrolling
                handler.postDelayed(this, 1000) // Adjust the delay as needed
            }
        }

        // Start auto-scrolling after a delay
        handler.postDelayed(runnable, 2000)

        // Disable manual scroll (touch events)
        viewPager.setOnTouchListener { _, _ -> true }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater).inflate(R.layout.image_slider_layout, null)
        val ivImages = view.findViewById<ImageView>(R.id.imageView)
        ivImages.setImageResource(imageList[getActualPosition(position)])

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}