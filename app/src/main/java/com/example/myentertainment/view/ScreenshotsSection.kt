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

        private const val DESIGN_WIDTH = 411f
        private const val DESIGN_HEIGHT = 731f

        private const val SCREENSHOT_BUTTON_DIMENSION = 70f
        private const val MARGIN = 12f
        private const val PADDING = 10f
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

    /**
     * Calculates horizontal dimension according to the device width in order to keep element's scale
     */
    private fun calcHorizontal(value: Float): Int {
        val dpWidth = context.resources.displayMetrics.widthPixels
        return (dpWidth * (value / DESIGN_WIDTH)).toInt()
    }

    /**
     * Calculates vertical dimension according to the device height in order to keep element's scale
     */
    private fun calcVertical(value: Float): Int {
        val dpHeight = context.resources.displayMetrics.heightPixels
        return (dpHeight * (value / DESIGN_HEIGHT)).toInt()
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
            val paddingHorizontal = calcHorizontal(PADDING)
            val paddingVertical = calcVertical(PADDING)
            screenshotButton.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
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
        val screenshotButtons = getScreenshotButtons()
        val screenshotsToMove = ArrayList<ImageView>()

        for (id in screenshotId until screenshotButtons.size) {
            val screenshotButton = screenshotButtons[id]
            screenshotsToMove.add(screenshotButton)
        }

        for (id in 1 until screenshotsToMove.size) {
            val source = screenshotsToMove[id]
            val destination = screenshotsToMove[id - 1]
            moveScreenshot(source, destination)
        }

        var lastVisibleScreenshotButton: ImageView
        if (loadedScreenshots < screenshotsLimit) {
            lastVisibleScreenshotButton = screenshotButtons[loadedScreenshots]
            hideScreenshotButton(lastVisibleScreenshotButton)
        }

        lastVisibleScreenshotButton = screenshotButtons[loadedScreenshots - 1]
        restoreScreenshotButton(lastVisibleScreenshotButton)
        loadedScreenshots--
    }

    /**
     * Returns max count of ScreenshotButtons that can fit in one horizontal line
     */
    private fun getMaxScreenshotButtonsPerLine(): Int {
        val maxWidth = context.resources.displayMetrics.widthPixels
        val screenshotButtonWidth = getParams().width + (calcHorizontal(PADDING) * 2)

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
        val screenshotButtonWidth = calcHorizontal(SCREENSHOT_BUTTON_DIMENSION)
        val screenshotButtonHeight = calcVertical(SCREENSHOT_BUTTON_DIMENSION)
        val params = LayoutParams(screenshotButtonWidth, screenshotButtonHeight)

        val marginHorizontal = calcHorizontal(MARGIN)
        val marginVertical = calcVertical(MARGIN)
        params.setMargins(marginHorizontal, marginVertical, marginHorizontal, marginVertical)

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