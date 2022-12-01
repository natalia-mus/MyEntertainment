package com.example.myentertainment.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.viewmodel.AboutViewModel

class About : AppCompatActivity() {

    private lateinit var viewModel: AboutViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about)

        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        viewModel.getDescription()
    }
}