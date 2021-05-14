package com.example.myentertainment.view.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.`object`.ValidationObject
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
    private lateinit var signInSection: ConstraintLayout

    private lateinit var email: String
    private lateinit var password: String

    private var message = ""

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

    private fun initView() {
        loadingSection = fragmentView.findViewById(R.id.signIn_loadingSection)
        signInSection = fragmentView.findViewById(R.id.signIn_section)
        emailEditText = fragmentView.findViewById(R.id.signIn_email)
        passwordEditText = fragmentView.findViewById(R.id.signIn_password)
        signInButton = fragmentView.findViewById(R.id.signIn_buttonOk)
        signUp = fragmentView.findViewById(R.id.signIn_signUp)

        signInButton.setOnClickListener() {
            email = emailEditText.text.toString()
            password = passwordEditText.text.toString()
            viewModel.signIn(email, password)
        }

        signUp.setOnClickListener() {
            onSignUpClickAction.signUpClicked()
        }
    }

    private fun setObservers() {
        viewModel.userId.observe(this, { updateActivity(it) })
        viewModel.loading.observe(this, { updateView(it) })
        viewModel.validationResult.observe(this, { validationResult(it) })
        viewModel.signingInStatus.observe(this, { signingInResult(it) })
    }

    private fun updateActivity(userId: String?) {
        if (userId != "null") {
            goToMainActivity()
        } else {
            loadingSection.visibility = View.INVISIBLE
            signInSection.visibility = View.VISIBLE
        }
    }

    private fun updateView(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.INVISIBLE
        }
    }

    private fun validationResult(validationResult: Int) {
        when (validationResult) {
            ValidationObject.EMPTY_VALUES -> message =
                getString(R.string.enter_both_email_and_password)
            ValidationObject.INVALID_EMAIL -> message =
                getString(R.string.your_email_address_is_invalid)
        }
        toast(message)
    }

    private fun signingInResult(signingUpResult: Boolean) {
        if (signingUpResult) goToMainActivity()
        else {
            val message = getString(R.string.failed_to_sign_in)
            toast(message)
        }
    }

    private fun goToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun toast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}


interface OnSignUpClickAction {
    fun signUpClicked()
}