package com.example.myentertainment.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myentertainment.R
import com.example.myentertainment.SharedPreferences
import com.example.myentertainment.view.authentication.AuthenticationActivity

class StartAppActivity : AppCompatActivity() {

    private lateinit var moreButton: Button
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button
    private lateinit var reportAProblemButton: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        prepareActivity()
    }

    private fun getShowStartAppActivity() = SharedPreferences.getShowStartAppActivity(this)

    private fun initView() {
        moreButton = findViewById(R.id.start_app_more)
        signInButton = findViewById(R.id.start_app_sign_in)
        signUpButton = findViewById(R.id.start_app_sign_up)
        reportAProblemButton = findViewById(R.id.start_app_report_a_problem)

        moreButton.setOnClickListener {
            startAboutActivity()
        }

        signInButton.setOnClickListener {
            startSignInActivity()
        }

        signUpButton.setOnClickListener {
            startSignUpActivity()
        }

        reportAProblemButton.setOnClickListener {
            startProblemReportActivity()
        }
    }

    private fun prepareActivity() {
        if (getShowStartAppActivity()) {
            setContentView(R.layout.activity_start_app)
            initView()
        } else {
            startSignInActivity()
        }
    }

    private fun startAboutActivity() {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

    private fun startSignInActivity() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
    }

    private fun startSignUpActivity() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.putExtra(AuthenticationActivity.SIGN_UP, true)
        startActivity(intent)
    }

    private fun startProblemReportActivity() {
        val intent = Intent(this, ProblemReportActivity::class.java)
        startActivity(intent)
    }
}