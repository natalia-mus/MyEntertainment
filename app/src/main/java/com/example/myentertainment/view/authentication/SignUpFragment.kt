package com.example.myentertainment.view.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.`object`.ValidationObject
import com.example.myentertainment.view.main.MainActivity
import com.example.myentertainment.viewmodel.SignUpFragmentViewModel

class SignUpFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewModel: SignUpFragmentViewModel

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var loadingSection: ConstraintLayout

    private lateinit var email: String
    private lateinit var password: String

    private var message = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        viewModel = ViewModelProvider(this).get(SignUpFragmentViewModel::class.java)
        initView()
        setObservers()
        return fragmentView
    }

    private fun initView() {
        emailEditText = fragmentView.findViewById(R.id.signUp_email)
        passwordEditText = fragmentView.findViewById(R.id.signUp_password)
        signUpButton = fragmentView.findViewById(R.id.signUp_buttonOk)
        loadingSection = fragmentView.findViewById(R.id.signUp_loadingSection)

        signUpButton.setOnClickListener() {
            email = emailEditText.text.toString()
            password = passwordEditText.text.toString()
            viewModel.signUp(email, password)
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this, { updateView(it) })
        viewModel.validationResult.observe(this, { validationResult(it) })
        viewModel.signingUpStatus.observe(this, { signingUpResult(it) })
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
            ValidationObject.EMPTY_VALUES -> {
                message = "Enter both e-mail and password"
            }
            ValidationObject.PASSWORD_TOO_SHORT -> {
                message = "Your password should be at least 6 characters long"
            }
            ValidationObject.INVALID_EMAIL -> {
                message = "Your e-mail address is invalid"
            }
        }
        toast(message)
    }

    private fun signingUpResult(signingUpStatus: Boolean) {
        if (signingUpStatus) {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        } else {
            message = "Unfortunately, user can not be created"
            toast(message)
        }
    }

    private fun toast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}