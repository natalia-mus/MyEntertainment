package com.example.myentertainment.view.problemreport

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.viewmodel.problemreport.ProblemReportViewModel

class ProblemReportActivity : AppCompatActivity() {

    private lateinit var viewModel: ProblemReportViewModel

    private lateinit var summaryEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var reportButton: Button
    private lateinit var screenshotFirst: ImageView
    private lateinit var screenshotSecond: ImageView
    private lateinit var screenshotThird: ImageView
    private lateinit var loadingSection: ConstraintLayout

    private var screenshots = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem_report)
        viewModel = ViewModelProvider(this).get(ProblemReportViewModel::class.java)
        setObservers()
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_CODE_CAPTURE_GALLERY_IMAGE -> {
                    if (data != null && data.data != null) {
                        val file = data.data!!
                        addScreenshot(file)
                    }
                }
            }
        }
    }

    private fun initView() {
        summaryEditText = findViewById(R.id.problemReportActivity_summary)
        descriptionEditText = findViewById(R.id.problemReportActivity_problemDescription)
        reportButton = findViewById(R.id.problemReportActivity_reportButton)
        screenshotFirst = findViewById(R.id.problemReportActivity_screenshot_1)
        screenshotSecond = findViewById(R.id.problemReportActivity_screenshot_2)
        screenshotThird = findViewById(R.id.problemReportActivity_screenshot_3)
        loadingSection = findViewById(R.id.problemReportActivity_loadingSection)

        reportButton.setOnClickListener() {
            sendReport()
        }

        screenshotFirst.setOnClickListener() {
            openGallery()
        }

        screenshotSecond.setOnClickListener() {
            openGallery()
        }

        screenshotThird.setOnClickListener() {
            openGallery()
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { updateView(it) }
        viewModel.validationResult.observe(this) { handleValidationResult(it) }
        viewModel.addingToDatabaseResult.observe(this) { handleAddingToDatabaseResult(it) }
    }

    private fun sendReport() {
        val summary = summaryEditText.text.toString()
        val description = descriptionEditText.text.toString()

        viewModel.addToDatabase(summary, description)
    }

    private fun updateView(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.GONE
        }
    }

    private fun handleAddingToDatabaseResult(result: Boolean) {
        if (result) {
            finish()
            Toast.makeText(applicationContext, getString(R.string.problem_report_has_been_sent), Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    private fun handleValidationResult(validationResult: ValidationResult) {
        when (validationResult) {
            ValidationResult.EMPTY_VALUES -> Toast.makeText(this, R.string.report_empty_fields, Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = Constants.INTENT_TYPE_IMAGE
        startActivityForResult(intent, Constants.REQUEST_CODE_CAPTURE_GALLERY_IMAGE)
    }

    private fun addScreenshot(file: Uri) {
        if (screenshots != 0) {
            var screenshot: ImageView? = null
            var nextScreenshot: ImageView? = null

            val screenshotId = 4 - screenshots

            when (screenshotId) {
                1 -> {
                    screenshot = screenshotFirst
                    nextScreenshot = screenshotSecond
                }
                2 -> {
                    screenshot = screenshotSecond
                    nextScreenshot = screenshotThird
                }
                3 -> {
                    screenshot = screenshotThird
                }
            }

            if (screenshot != null) {
                Glide.with(this)
                    .load(file)
                    .into(screenshot)

                screenshot.setOnClickListener() {
                    deleteScreenshot(screenshotId)
                }
            }

            nextScreenshot?.visibility = View.VISIBLE

            screenshots--
        }
    }

    private fun deleteScreenshot(screenshotId: Int) {
        screenshots++

        when (screenshotId) {
            1 -> {
                when (screenshots) {
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
                when (screenshots) {
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


    private fun hideScreenshotButton(screenshot: ImageView) {
        screenshot.visibility = View.GONE
    }

    /**
     * Gets drawable from source screenshot button and sets it as a drawable of destination screenshot button
     */
    private fun moveScreenshot(sourceScreenshot: ImageView, destinationScreenshot: ImageView) {
        val newScreenshot = sourceScreenshot.drawable

        Glide.with(this)
            .load(newScreenshot)
            .into(destinationScreenshot)
    }

    /**
     * Restores default look and functionality of a screenshot button
     */
    private fun restoreScreenshotButton(screenshot: ImageView) {
        screenshot.setImageResource(R.drawable.ic_add_photo)

        screenshot.setOnClickListener() {
            openGallery()
        }
    }

}