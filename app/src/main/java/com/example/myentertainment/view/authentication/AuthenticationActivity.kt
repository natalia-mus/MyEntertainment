package com.example.myentertainment.view.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myentertainment.R

class AuthenticationActivity : AppCompatActivity(), OnSignUpClickAction {

    private val signInFragment = SignInFragment(this)
    private val signUpFragment = SignUpFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        changeCurrentFragment(signInFragment)
    }

    private fun changeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.authenticationActivity_fragment, fragment)
            commit()
        }
    }

    override fun signUpClicked() {
        changeCurrentFragment(signUpFragment)
    }
}