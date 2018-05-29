package com.ryanmounla.mvvmarch.util

import android.content.res.Resources
import android.graphics.Rect
import android.view.View

/**
 * Created by ryanmounla on 2018-05-28.
 */

object SystemUtils {

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun isViewOverlapping(firstView: View, secondView: View): Boolean {
        val firstPosition = IntArray(2)
        val secondPosition = IntArray(2)

        firstView.getLocationOnScreen(firstPosition)
        secondView.getLocationOnScreen(secondPosition)

        val rectFirstView = Rect(firstPosition[0], firstPosition[1],
                firstPosition[0] + firstView.measuredWidth, firstPosition[1] + firstView.measuredHeight)
        val rectSecondView = Rect(secondPosition[0], secondPosition[1],
                secondPosition[0] + secondView.measuredWidth, secondPosition[1] + secondView.measuredHeight)
        return rectFirstView.intersect(rectSecondView)
    }

}
