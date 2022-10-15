package com.example.myentertainment.view.problemreport

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.viewmodel.problemreport.ProblemReportViewModel

class ProblemReportActivity : AppCompatActivity() {

    private lateinit var viewModel: ProblemReportViewModel

    private lateinit var summaryEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var reportButton: Button
    private lateinit var loadingSection: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem_report)
        viewModel = ViewModelProvider(this).get(ProblemReportViewModel::class.java)
        setObservers()
        initView()
    }

    private fun initView() {
        summaryEditText = findViewById(R.id.problemReportActivity_summary)
        descriptionEditText = findViewById(R.id.problemReportActivity_problemDescription)
        reportButton = findViewById(R.id.problemReportActivity_reportButton)
        loadingSection = findViewById(R.id.problemReportActivity_loadingSection)

        reportButton.setOnClickListener() {
            sendReport()
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { updateView(it) }
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

}