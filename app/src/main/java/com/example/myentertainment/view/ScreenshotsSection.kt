package com.example.myentertainment.view

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.AttributeSet
import android.view.Gravity
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.example.myentertainment.LayoutDimensionsUtil
import com.example.myentertainment.R
import java.util.*
import kotlin.math.floor


class ScreenshotsSection @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SCREENSHOTS_LIMIT = 1

        private const val SCREENSHOT_BUTTON_DIMENSION = 70f
        private const val MARGIN = 12f
        private const val PADDING = 10f
    }

    private val attributes = context.obtainStyledAttributes(attrs, R.styleable.ScreenshotsSection)

    private var onEmptyScreenshotButtonClickListener: OnClickListener? = null
    private var screenshotsLimit = attributes.getInt(R.styleable.ScreenshotsSection_screenshotsLimit, DEFAULT_SCREENSHOTS_LIMIT)
    private val loadedScreenshots = HashMap<UUID, Uri>()

    init {
        orientation = VERTICAL
        createScreenshotButton()
    }


    fun addScreenshot(file: Uri) {
        if (loadedScreenshots.size < screenshotsLimit) {
            val screenshotButton = getScreenshotButton(loadedScreenshots.size)
            val screenshot = screenshotButton.getViewById(R.id.screenshotButton_image) as ImageView

            Glide.with(this)
                .load(file)
                .into(screenshot)

            val uuid = UUID.randomUUID()
            screenshot.setOnClickListener(getOnDeleteScreenshotClickListener(loadedScreenshots.size, uuid))
            setDeleteIconVisible(screenshotButton)
            createScreenshotButton()
            loadedScreenshots.put(uuid, file)
        }
    }

    fun getScreenshots(): HashMap<UUID, Uri> {
        return loadedScreenshots
    }

    fun setOnEmptyScreenshotButtonClickListener(listener: OnClickListener) {
        onEmptyScreenshotButtonClickListener = listener

        for (screenshotButton in getScreenshotButtons()) {
            screenshotButton.setOnClickListener(onEmptyScreenshotButtonClickListener)
        }
    }

    private fun createScreenshotButton() {
        if (getScreenshotButtons().size < screenshotsLimit) {
            if (childCount == 0) {
                val firstLine = newLine()
                addView(firstLine)
            }

            val maxScreenshotButtonsPerLine = getMaxScreenshotButtonsPerLine()


            // screenshotButton - parent
            val screenshotButton = ConstraintLayout(context)
            screenshotButton.id = R.id.screenshotButton
            screenshotButton.layoutParams = getParams()
            screenshotButton.setOnClickListener(onEmptyScreenshotButtonClickListener)

            // image visible inside screenshotButton
            val image = ImageView(context)
            image.id = R.id.screenshotButton_image
            image.layoutParams = getParams()
            image.setImageResource(R.drawable.ic_add_photo)
            image.setBackgroundResource(R.drawable.background_outlined_text_field)
            val imagePaddingHorizontal = LayoutDimensionsUtil.calcWidth(context, PADDING)
            val imagePaddingVertical = LayoutDimensionsUtil.calcHeight(context, PADDING)
            image.setPadding(imagePaddingHorizontal, imagePaddingVertical, imagePaddingHorizontal, imagePaddingVertical)

            // deleteIcon, visible only when screenshot added
            val deleteIcon = ImageView(context)
            deleteIcon.id = R.id.screenshotButton_deleteIcon
            val iconWidth = LayoutDimensionsUtil.calcWidth(context, SCREENSHOT_BUTTON_DIMENSION * 0.45f)
            val iconHeight = LayoutDimensionsUtil.calcHeight(context, SCREENSHOT_BUTTON_DIMENSION * 0.45f)
            deleteIcon.layoutParams = LayoutParams(iconWidth, iconHeight)
            deleteIcon.setColorFilter(Color.RED)
            val paddingEnd = LayoutDimensionsUtil.calcWidth(context, MARGIN * 0.5f)
            val paddingBottom = LayoutDimensionsUtil.calcHeight(context, MARGIN * 0.5f)
            deleteIcon.setPadding(0, 0, paddingEnd, paddingBottom)

            screenshotButton.addView(image)
            screenshotButton.addView(deleteIcon)

            val constraints = ConstraintSet()
            constraints.clone(screenshotButton)
            constraints.connect(deleteIcon.id, ConstraintSet.END, screenshotButton.id, ConstraintSet.END)
            constraints.connect(deleteIcon.id, ConstraintSet.BOTTOM, screenshotButton.id, ConstraintSet.BOTTOM)
            constraints.applyTo(screenshotButton)

            screenshotButton.setConstraintSet(constraints)


            val lastLine = children.last()
            val screenshotButtonsPerLine = (lastLine as LinearLayout).childCount

            if (screenshotButtonsPerLine == maxScreenshotButtonsPerLine) {
                val newLine = newLine()
                newLine.addView(screenshotButton)
                addView(newLine)

            } else {
                lastLine.addView(screenshotButton)
            }
        }

    }

    private fun deleteScreenshot(screenshotId: Int, screenshotUUID: UUID) {
        val screenshotButtons = getScreenshotButtons()
        val screenshotsToMove = ArrayList<ConstraintLayout>()

        for (id in screenshotId until screenshotButtons.size) {
            val screenshotButton = screenshotButtons[id]
            screenshotsToMove.add(screenshotButton)
        }

        for (id in 1 until screenshotsToMove.size) {
            val source = screenshotsToMove[id]
            val destination = screenshotsToMove[id - 1]
            moveScreenshot(source, destination)
        }

        var lastScreenshotButton: ConstraintLayout
        if (loadedScreenshots.size < screenshotsLimit) {
            lastScreenshotButton = screenshotButtons[loadedScreenshots.size]
            removeScreenshotButton(lastScreenshotButton)
        }

        lastScreenshotButton = screenshotButtons[loadedScreenshots.size - 1]
        removeScreenshotButton(lastScreenshotButton)
        createScreenshotButton()

        loadedScreenshots.remove(screenshotUUID)
    }

    /**
     * Returns max count of ScreenshotButtons that can fit in one horizontal line
     */
    private fun getMaxScreenshotButtonsPerLine(): Int {
        val maxWidth = context.resources.displayMetrics.widthPixels
        val screenshotButtonWidth = getParams().width + (LayoutDimensionsUtil.calcWidth(context, PADDING) * 2)

        val result = (maxWidth / screenshotButtonWidth).toDouble()
        return floor(result).toInt()
    }

    private fun getOnDeleteScreenshotClickListener(screenshotId: Int, screenshotUUID: UUID): OnClickListener {
        return OnClickListener { deleteScreenshot(screenshotId, screenshotUUID) }
    }

    /**
     * Returns ScreenshotButton params
     */
    private fun getParams(): LayoutParams {
        val screenshotButtonWidth = LayoutDimensionsUtil.calcWidth(context, SCREENSHOT_BUTTON_DIMENSION)
        val screenshotButtonHeight = LayoutDimensionsUtil.calcHeight(context, SCREENSHOT_BUTTON_DIMENSION)
        val params = LayoutParams(screenshotButtonWidth, screenshotButtonHeight)

        val marginHorizontal = LayoutDimensionsUtil.calcWidth(context, MARGIN)
        val marginVertical = LayoutDimensionsUtil.calcHeight(context, MARGIN)
        params.setMargins(marginHorizontal, marginVertical, marginHorizontal, marginVertical)

        return params
    }

    private fun getScreenshotButton(screenshotId: Int): ConstraintLayout {
        val screenshotButtons = getScreenshotButtons()
        return screenshotButtons[screenshotId]
    }

    private fun getScreenshotButtons(): ArrayList<ConstraintLayout> {
        val result = ArrayList<ConstraintLayout>()

        for (line in children) {
            val screenshotButtons = (line as LinearLayout).children

            for (screenshotButton in screenshotButtons) {
                result.add(screenshotButton as ConstraintLayout)
            }
        }

        return result
    }

    /**
     * Gets drawable from source screenshot button and sets it as a drawable of destination screenshot button
     */
    private fun moveScreenshot(sourceScreenshotButton: ConstraintLayout, destinationScreenshotButton: ConstraintLayout) {
        val sourceScreenshot = sourceScreenshotButton.getViewById(R.id.screenshotButton_image) as ImageView
        val destinationScreenshot = destinationScreenshotButton.getViewById(R.id.screenshotButton_image) as ImageView
        val newScreenshot = sourceScreenshot.drawable

        Glide.with(this)
            .load(newScreenshot)
            .into(destinationScreenshot)
    }

    /**
     * Creates new line of screenshotButtons
     */
    private fun newLine(): LinearLayout {
        val line = LinearLayout(context)
        line.gravity = Gravity.CENTER
        return line
    }

    private fun removeScreenshotButton(screenshotButton: ConstraintLayout) {
        for (line in children) {
            val linearLine = line as LinearLayout

            for (button in linearLine.children) {
                if (button == screenshotButton) {
                    linearLine.removeView(button)
                }
            }

            if (linearLine.childCount == 0) {
                removeView(linearLine)
            }
        }
    }

    /**
     * Makes deleteIcon visible by changing its drawable resource
     * Setting actual visibility by setVisibility() method is ignored in ConstraintLayout
     */
    private fun setDeleteIconVisible(screenshotButton: ConstraintLayout) {
        val deleteIcon = screenshotButton.getViewById(R.id.screenshotButton_deleteIcon) as ImageView
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_delete, null)
        deleteIcon.setImageDrawable(drawable)
    }

}