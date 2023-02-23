package com.example.myentertainment.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.example.myentertainment.R

class ScreenshotsSection @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        createScreenshotButtons()
    }

    private fun createScreenshotButtons() {
        val params = LayoutParams(140, 140)
        params.marginStart = 20
        params.marginEnd = 20
        params.topMargin = 14
        params.bottomMargin = 40

        for (index in 0..2) {
            val screenshotButton = ImageView(context)
            screenshotButton.layoutParams = params
            screenshotButton.setImageResource(R.drawable.ic_add_photo)
            screenshotButton.setBackgroundResource(R.drawable.background_outlined_text_field)
            screenshotButton.setPadding(8)
            if (index > 0) screenshotButton.visibility = View.GONE
            addView(screenshotButton)
        }

    }
}