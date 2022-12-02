package com.example.myentertainment.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.viewmodel.AboutViewModel

class About : AppCompatActivity() {

    private lateinit var viewModel: AboutViewModel

    private lateinit var descriptionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about)

        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        initView()
        setObservers()
        viewModel.getDescription()
    }

    private fun setObservers() {
        viewModel.loading.observe(this) { handleLoadingStatus(it) }
        viewModel.description.observe(this) {updateView(it)}
    }

    private fun handleLoadingStatus(loading: Boolean) {
        if (loading) {
            // show
        } else {
            // hide
        }
    }

    private fun initView() {
        descriptionTextView = findViewById(R.id.about_description)
    }

    private fun updateView(description: String) {
        descriptionTextView.text = description
    }
}