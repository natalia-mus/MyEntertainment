package com.example.myentertainment.view.problemreport

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.data.ProblemReport
import com.example.myentertainment.viewmodel.problemreport.ProblemReportViewModel

class ProblemReportActivity : AppCompatActivity() {

    private lateinit var viewModel: ProblemReportViewModel

    private lateinit var summaryEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var reportButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem_report)
        viewModel = ViewModelProvider(this).get(ProblemReportViewModel::class.java)
        initView()
    }

    private fun initView() {
        summaryEditText = findViewById(R.id.problemReportActivity_summary)
        descriptionEditText = findViewById(R.id.problemReportActivity_problemDescription)
        reportButton = findViewById(R.id.problemReportActivity_reportButton)

        reportButton.setOnClickListener() {
            sendReport()
        }
    }

    private fun sendReport() {
        val summary = summaryEditText.text.toString()
        val description = descriptionEditText.text.toString()

        val report = ProblemReport(null, null, summary, description)
        viewModel.addToDatabase(report)
    }
}