package com.example.myentertainment

import android.content.Context
import android.util.TypedValue

object LayoutDimensionsUtil {

    private const val DESIGN_HEIGHT = 731f
    private const val DESIGN_WIDTH = 411f


    /**
     * Calculates vertical dimension according to the device height in order to keep element's scale
     */
    fun calcHeight(context: Context, value: Float): Int {
        val dpHeight = context.resources.displayMetrics.heightPixels
        return (dpHeight * (value / DESIGN_HEIGHT)).toInt()
    }

    /**
     * Calculates vertical dimension according to the device height in order to keep element's scale
     */
    fun calcHeight(context: Context, value: Int): Int {
        val dpHeight = context.resources.displayMetrics.heightPixels
        return (dpHeight * (value / DESIGN_HEIGHT)).toInt()
    }

    /**
     * Calculates horizontal dimension according to the device width in order to keep element's scale
     */
    fun calcWidth(context: Context, value: Float): Int {
        val dpWidth = context.resources.displayMetrics.widthPixels
        return (dpWidth * (value / DESIGN_WIDTH)).toInt()
    }

    /**
     * Calculates horizontal dimension according to the device width in order to keep element's scale
     */
    fun calcWidth(context: Context, value: Int): Int {
        val dpWidth = context.resources.displayMetrics.widthPixels
        return (dpWidth * (value / DESIGN_WIDTH)).toInt()
    }

    fun calcTextSize(context: Context, value: Int): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), context.resources.displayMetrics)
    }

}