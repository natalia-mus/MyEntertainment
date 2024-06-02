package com.example.myentertainment.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
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
    private lateinit var historyTextView: TextView
    private lateinit var githubButton: Button
    private lateinit var loadingSection: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        initView()
        setObservers()
        viewModel.getData()
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
        historyTextView = findViewById(R.id.about_history)
        githubButton = findViewById(R.id.about_github)
        loadingSection = findViewById(R.id.about_loadingSection)

        githubButton.setOnClickListener {
            openUrl()
        }
    }

    private fun openUrl() {
        val url = viewModel.about.value?.url
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun setObservers() {
        viewModel.fetchingDataStatus.observe(this) { handleFetchingDataStatus(it) }
        viewModel.about.observe(this) { updateView(it) }
    }

    private fun updateView(about: About) {
        descriptionTextView.text = about.description
        historyTextView.text = about.history

        descriptionTextView.visibility = View.VISIBLE
        historyTextView.visibility = View.VISIBLE
        githubButton.visibility = View.VISIBLE
    }

}