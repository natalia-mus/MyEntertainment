package com.example.myentertainment.view.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.Constants
import com.example.myentertainment.R
import com.example.myentertainment.SharedPreferences
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.view.main.MainActivity
import com.example.myentertainment.viewmodel.authentication.SignInFragmentViewModel

class SignInFragment(private val onSignUpClickAction: OnSignUpClickAction) : Fragment() {

    private lateinit var viewModel: SignInFragmentViewModel
    private lateinit var fragmentView: View
    private lateinit var signInButton: Button
    private lateinit var signUp: TextView
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loadingSection: ConstraintLayout
    private lateinit var signInSection: ScrollView

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_sign_in, container, false)
        initView()
        viewModel = ViewModelProvider(this).get(SignInFragmentViewModel::class.java)
        setObservers()
        viewModel.checkUserId()
        return fragmentView
    }

    private fun goToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        SharedPreferences.setShowStartAppActivity(requireContext(), false)
    }

    private fun handleSignInResult(signInResult: Boolean) {
        if (signInResult) {
            goToMainActivity()
        } else {
            val message = getString(R.string.failed_to_sign_in)
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun handleValidationResult(validationResult: ValidationResult) {
        var message = ""

        when (validationResult) {
            ValidationResult.EMPTY_VALUES -> message =
                getString(R.string.enter_all_required_values)
            ValidationResult.INVALID_EMAIL -> message =
                getString(R.string.your_email_address_is_invalid)
        }
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun initView() {
        loadingSection = fragmentView.findViewById(R.id.signIn_loadingSection)
        signInSection = fragmentView.findViewById(R.id.signIn_section)
        emailEditText = fragmentView.findViewById(R.id.signIn_email)
        passwordEditText = fragmentView.findViewById(R.id.signIn_password)
        signInButton = fragmentView.findViewById(R.id.signIn_buttonOk)
        signUp = fragmentView.findViewById(R.id.signIn_signUp)

        signInButton.setOnClickListener() {
            //signInAsTestUser()
            email = emailEditText.text.toString()
            password = passwordEditText.text.toString()
            viewModel.signIn(email, password)
        }

        signUp.setOnClickListener() {
            onSignUpClickAction.signUpClicked()
        }
    }

    private fun setObservers() {
        viewModel.userId.observe(this) { updateActivity(it) }
        viewModel.loading.observe(this) { updateView(it) }
        viewModel.validationResult.observe(this) { handleValidationResult(it) }
        viewModel.signInResult.observe(this) { handleSignInResult(it) }
    }

    // method to improve logging in as one of the test users with the same domain and password
    private fun signInAsTestUser() {
        val domain = "@example.com"
        val password = ""

        emailEditText.text = emailEditText.text.append(domain)
        passwordEditText.setText(password)
    }

    private fun updateActivity(userId: String?) {
        if (userId != Constants.NULL) {
            goToMainActivity()
        } else {
            loadingSection.visibility = View.GONE
            signInSection.visibility = View.VISIBLE
        }
    }

    private fun updateView(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.GONE
        }
    }

}


interface OnSignUpClickAction {
    fun signUpClicked()
}