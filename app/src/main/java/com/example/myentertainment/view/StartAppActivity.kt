package com.example.myentertainment.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myentertainment.R
import com.example.myentertainment.view.authentication.AuthenticationActivity

class StartAppActivity : AppCompatActivity() {

    private lateinit var moreButton: Button
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_start_app)
        initView()
    }

    private fun initView() {
        moreButton = findViewById(R.id.start_app_more)
        signInButton = findViewById(R.id.start_app_sign_in)
        signUpButton = findViewById(R.id.start_app_sign_up)

        moreButton.setOnClickListener {
            startAboutActivity()
        }

        signInButton.setOnClickListener {
            startSignInActivity()
        }

        signUpButton.setOnClickListener {
            startSignUpActivity()
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
}