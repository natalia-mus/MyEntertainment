package com.example.myentertainment.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.example.myentertainment.R
import kotlin.math.floor


class ScreenshotsSection @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SCREENSHOTS_LIMIT = 1

        private const val SCREENSHOT_BUTTON_DIMENSION = 160
        private const val MARGIN = 24
        private const val PADDING = 22
    }

    private val attributes = context.obtainStyledAttributes(attrs, R.styleable.ScreenshotsSection)

    private var onEmptyScreenshotButtonClickListener: OnClickListener? = null
    private var screenshotsLimit = attributes.getInt(R.styleable.ScreenshotsSection_screenshotsLimit, DEFAULT_SCREENSHOTS_LIMIT)
    private var loadedScreenshots = 0

    init {
        orientation = VERTICAL
        createScreenshotButtons()
    }


    fun addScreenshot(file: Uri) {
        if (loadedScreenshots < screenshotsLimit) {
            val screenshotButtons = getScreenshotButtons()

            val screenshot = screenshotButtons[loadedScreenshots]

            Glide.with(this)
                .load(file)
                .into(screenshot)

            screenshot.setOnClickListener(getOnDeleteScreenshotClickListener(loadedScreenshots))

            loadedScreenshots++

            if (loadedScreenshots < screenshotButtons.size) {
                val nextScreenshot = screenshotButtons[loadedScreenshots]
                nextScreenshot.visibility = View.VISIBLE
            }
        }
    }

    fun setOnEmptyScreenshotButtonClickListener(listener: OnClickListener) {
        onEmptyScreenshotButtonClickListener = listener

        for (screenshotButton in children) {
            screenshotButton.setOnClickListener(onEmptyScreenshotButtonClickListener)
        }
    }

    private fun createScreenshotButtons() {
        val maxScreenshotButtonsPerLine = getMaxScreenshotButtonsPerLine()
        var screenshotButtonsPerLine = 0
        var line = newLine()

        for (index in 0 until screenshotsLimit) {
            val screenshotButton = ImageView(context)
            screenshotButton.layoutParams = getParams()
            screenshotButton.setImageResource(R.drawable.ic_add_photo)
            screenshotButton.setBackgroundResource(R.drawable.background_outlined_text_field)
            screenshotButton.setPadding(PADDING)
            if (index > 0) screenshotButton.visibility = View.GONE
            line.addView(screenshotButton)
            screenshotButtonsPerLine++

            if (screenshotButtonsPerLine == maxScreenshotButtonsPerLine) {
                // next ScreenshotButton won't fit in the same line
                addView(line)
                line = newLine()
                screenshotButtonsPerLine = 0

            } else if (index == screenshotsLimit - 1) {
                addView(line)
            }
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

    /**
     * Returns max count of ScreenshotButtons that can fit in one horizontal line
     */
    private fun getMaxScreenshotButtonsPerLine(): Int {
        val maxWidth = context.resources.displayMetrics.widthPixels
        val screenshotButtonWidth = getParams().width + (PADDING * 2)

        val result = (maxWidth / screenshotButtonWidth).toDouble()
        return floor(result).toInt()
    }

    private fun getOnDeleteScreenshotClickListener(screenshotId: Int): OnClickListener {
        return OnClickListener { deleteScreenshot(screenshotId) }
    }

    /**
     * Returns ScreenshotButton params
     */
    private fun getParams(): LayoutParams {
        val params = LayoutParams(SCREENSHOT_BUTTON_DIMENSION, SCREENSHOT_BUTTON_DIMENSION)
        params.setMargins(MARGIN)

        return params
    }

    private fun getScreenshotButtons(): ArrayList<ImageView> {
        val result = ArrayList<ImageView>()

        for (line in children) {
            val screenshotButtons = (line as LinearLayout).children

            for (screenshotButton in screenshotButtons) {
                result.add(screenshotButton as ImageView)
            }
        }

        return result
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

    private fun newLine(): LinearLayout {
        val line = LinearLayout(context)
        line.gravity = Gravity.CENTER
        return line
    }

    /**
     * Restores default look and functionality of a screenshot button
     */
    private fun restoreScreenshotButton(screenshotButton: ImageView) {
        screenshotButton.setImageResource(R.drawable.ic_add_photo)
        screenshotButton.setOnClickListener(onEmptyScreenshotButtonClickListener)
    }

}