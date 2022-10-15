package com.example.myentertainment.view.problemreport

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.interfaces.AddFragmentViewModelInterface
import com.example.myentertainment.viewmodel.problemreport.ProblemReportViewModel

class ProblemReportActivity : AppCompatActivity(), AddFragmentViewModelInterface {

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

    override fun initView() {
        summaryEditText = findViewById(R.id.problemReportActivity_summary)
        descriptionEditText = findViewById(R.id.problemReportActivity_problemDescription)
        reportButton = findViewById(R.id.problemReportActivity_reportButton)
        loadingSection = findViewById(R.id.problemReportActivity_loadingSection)

        reportButton.setOnClickListener() {
            sendReport()
        }
    }

    override fun setObservers() {
        viewModel.loading.observe(this) { updateView(it, loadingSection) }
    }

    private fun sendReport() {
        val summary = summaryEditText.text.toString()
        val description = descriptionEditText.text.toString()

        viewModel.addToDatabase(summary, description)
    }
}