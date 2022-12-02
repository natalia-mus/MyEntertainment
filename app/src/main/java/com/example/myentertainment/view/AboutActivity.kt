package com.example.myentertainment.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.data.About
import com.example.myentertainment.viewmodel.AboutViewModel

class AboutActivity : AppCompatActivity() {

    private lateinit var viewModel: AboutViewModel

    private lateinit var descriptionTextView: TextView
    private lateinit var loadingSection: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        initView()
        setObservers()
        viewModel.getDescription()
    }

    private fun setObservers() {
        viewModel.fetchingDataStatus.observe(this) { handleFetchingDataStatus(it) }
        viewModel.about.observe(this) { updateView(it) }
    }

    private fun handleFetchingDataStatus(status: Boolean) {
        if (status) {
            loadingSection.visibility = View.GONE
        } else {
            finish()
            Toast.makeText(applicationContext, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
        }
    }

    private fun initView() {
        descriptionTextView = findViewById(R.id.about_description)
        loadingSection = findViewById(R.id.about_loadingSection)
    }

    private fun updateView(about: About) {
        descriptionTextView.text = about.description
    }
}