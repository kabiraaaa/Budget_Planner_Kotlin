package com.sample.budgetplanner

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class OnBoardingViewPagerAnim : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.apply {
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    // Slide the page
                    translationX = -position * width

                    // Fade the page if it's not the current page
                    alpha = Math.max(0f, 1 - Math.abs(position))
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}