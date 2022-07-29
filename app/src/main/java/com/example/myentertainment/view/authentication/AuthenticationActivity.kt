package com.example.myentertainment.view.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myentertainment.Constants
import com.example.myentertainment.R

class AuthenticationActivity : AppCompatActivity(), OnSignUpClickAction {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        if (intent.getBooleanExtra(Constants.IS_PASSWORD_CHANGE, false)) {
            changeCurrentFragment(ChangePasswordFragment())
        } else {
            changeCurrentFragment(SignInFragment(this))
        }
    }

    private fun changeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.authenticationActivity_fragment, fragment)
            commit()
        }
    }

    override fun signUpClicked() {
        changeCurrentFragment(SignUpFragment())
    }
}