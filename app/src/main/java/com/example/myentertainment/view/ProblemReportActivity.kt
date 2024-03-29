package com.example.myentertainment.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.viewmodel.ProblemReportViewModel

class ProblemReportActivity : AppCompatActivity() {

    private lateinit var viewModel: ProblemReportViewModel

    private lateinit var summaryEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var reportButton: Button
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var screenshotsSection: ScreenshotsSection


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
                        screenshotsSection.addScreenshot(file)
                    }
                }
            }
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

    private fun initView() {
        summaryEditText = findViewById(R.id.problemReportActivity_summary)
        descriptionEditText = findViewById(R.id.problemReportActivity_problemDescription)
        reportButton = findViewById(R.id.problemReportActivity_reportButton)
        loadingSection = findViewById(R.id.problemReportActivity_loadingSection)
        screenshotsSection = findViewById(R.id.problemReportActivity_screenshotsSection)

        reportButton.setOnClickListener() {
            sendReport()
        }

        setOnEmptyScreenshotButtonClickListener()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = Constants.INTENT_TYPE_IMAGE
        startActivityForResult(intent, Constants.REQUEST_CODE_CAPTURE_GALLERY_IMAGE)
    }

    private fun sendReport() {
        val summary = summaryEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val screenshots = screenshotsSection.getScreenshots()

        viewModel.addToDatabase(summary, description, screenshots)
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { updateView(it) }
        viewModel.validationResult.observe(this) { handleValidationResult(it) }
        viewModel.addingToDatabaseResult.observe(this) { handleAddingToDatabaseResult(it) }
    }

    private fun setOnEmptyScreenshotButtonClickListener() {
        val onEmptyScreenshotButtonClickListener = OnClickListener() {
            openGallery()
        }

        screenshotsSection.setOnEmptyScreenshotButtonClickListener(onEmptyScreenshotButtonClickListener)
    }

    private fun updateView(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.GONE
        }
    }

}