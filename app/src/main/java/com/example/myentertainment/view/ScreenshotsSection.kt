package com.example.myentertainment.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.example.myentertainment.R

class ScreenshotsSection @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val typedArray = context.obtainStyledAttributes(attrs, R.styleable.screenshotsSection)

    private var onEmptyScreenshotButtonClickListener: OnClickListener? = null
    private var screenshotsLimit = typedArray.getInt(R.styleable.screenshotsSection_screenshotsLimit, 3)

    init {
        createScreenshotButtons()
    }


    fun addScreenshot(file: Uri) {
        if (screenshotsLimit != 0) {
            var screenshot: ImageView? = null
            var nextScreenshot: ImageView? = null

            val screenshotId = 4 - screenshotsLimit

            when (screenshotId) {
                1 -> {
                    screenshot = getChildAt(0) as ImageView
                    nextScreenshot = getChildAt(1) as ImageView
                }
                2 -> {
                    screenshot = getChildAt(1) as ImageView
                    nextScreenshot = getChildAt(2) as ImageView
                }
                3 -> {
                    screenshot = getChildAt(2) as ImageView
                }
            }

            if (screenshot != null) {
                Glide.with(this)
                    .load(file)
                    .into(screenshot)

                screenshot.setOnClickListener(getOnDeleteScreenshotClickListener(screenshotId))
            }

            nextScreenshot?.visibility = View.VISIBLE

            screenshotsLimit--
        }
    }

    fun setOnEmptyScreenshotButtonClickListener(listener: OnClickListener) {
        onEmptyScreenshotButtonClickListener = listener

        for (screenshotButton in children) {
            screenshotButton.setOnClickListener(onEmptyScreenshotButtonClickListener)
        }
    }

    private fun createScreenshotButtons() {
        val params = LayoutParams(160, 160)
        params.marginStart = 24
        params.marginEnd = 24
        params.topMargin = 14
        params.bottomMargin = 40

        for (index in 0..2) {
            val screenshotButton = ImageView(context)
            screenshotButton.layoutParams = params
            screenshotButton.setImageResource(R.drawable.ic_add_photo)
            screenshotButton.setBackgroundResource(R.drawable.background_outlined_text_field)
            screenshotButton.setPadding(22)
            if (index > 0) screenshotButton.visibility = View.GONE
            addView(screenshotButton)
        }

    }

    private fun deleteScreenshot(screenshotId: Int) {
        screenshotsLimit++

        val screenshotFirst = getChildAt(0) as ImageView
        val screenshotSecond = getChildAt(1) as ImageView
        val screenshotThird = getChildAt(2) as ImageView

        when (screenshotId) {
            1 -> {
                when (screenshotsLimit) {
                    3 -> {
                        restoreScreenshotButton(screenshotFirst)
                        hideScreenshotButton(screenshotSecond)

                    }
                    2 -> {
                        moveScreenshot(screenshotSecond, screenshotFirst)
                        restoreScreenshotButton(screenshotSecond)
                        hideScreenshotButton(screenshotThird)

                    }
                    1 -> {
                        moveScreenshot(screenshotSecond, screenshotFirst)
                        moveScreenshot(screenshotThird, screenshotSecond)
                        restoreScreenshotButton(screenshotThird)
                    }
                }

            }
            2 -> {
                when (screenshotsLimit) {
                    2 -> {
                        restoreScreenshotButton(screenshotSecond)
                        hideScreenshotButton(screenshotThird)
                    }
                    1 -> {
                        moveScreenshot(screenshotThird, screenshotSecond)
                        restoreScreenshotButton(screenshotThird)
                    }
                }

            }
            3 -> {
                restoreScreenshotButton(screenshotThird)
            }
        }

    }

    private fun getOnDeleteScreenshotClickListener(screenshotId: Int): OnClickListener {
        return OnClickListener { deleteScreenshot(screenshotId) }
    }

    private fun hideScreenshotButton(screenshotButton: ImageView) {
        screenshotButton.visibility = View.GONE
    }

    /**
     * Gets drawable from source screenshot button and sets it as a drawable of destination screenshot button
     */
    private fun moveScreenshot(sourceScreenshotButton: ImageView, destinationScreenshotButton: ImageView) {
        val newScreenshot = sourceScreenshotButton.drawable

        Glide.with(this)
            .load(newScreenshot)
            .into(destinationScreenshotButton)
    }

    /**
     * Restores default look and functionality of a screenshot button
     */
    private fun restoreScreenshotButton(screenshotButton: ImageView) {
        screenshotButton.setImageResource(R.drawable.ic_add_photo)
        screenshotButton.setOnClickListener(onEmptyScreenshotButtonClickListener)
    }

}