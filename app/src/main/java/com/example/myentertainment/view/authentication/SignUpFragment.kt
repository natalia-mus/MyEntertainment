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
import com.example.myentertainment.viewmodel.authentication.SignUpFragmentViewModel

class SignUpFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewModel: SignUpFragmentViewModel

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var loadingSection: ConstraintLayout

    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var confirmPassword: String

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
        usernameEditText = fragmentView.findViewById(R.id.signUp_username)
        emailEditText = fragmentView.findViewById(R.id.signUp_email)
        passwordEditText = fragmentView.findViewById(R.id.signUp_password)
        confirmPasswordEditText = fragmentView.findViewById(R.id.signUp_confirmPassword)
        signUpButton = fragmentView.findViewById(R.id.signUp_buttonOk)
        loadingSection = fragmentView.findViewById(R.id.signUp_loadingSection)

        signUpButton.setOnClickListener() {
            //signUpAsTestUser()
            username = usernameEditText.text.toString()
            email = emailEditText.text.toString()
            password = passwordEditText.text.toString()
            confirmPassword = confirmPasswordEditText.text.toString()
            viewModel.signUp(username, email, password, confirmPassword)
        }
    }

    private fun setObservers() {
        viewModel.loading.observe(this, { updateView(it) })
        viewModel.validationResult.observe(this, { validationResult(it) })
        viewModel.signingUpStatus.observe(this, { signingUpResult(it) })
    }

    private fun signingUpResult(signingUpStatus: Boolean) {
        if (signingUpStatus) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            val message = getString(R.string.user_can_not_be_created)
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    // method to improve signing up as one of the test users with the same domain and password
    private fun signUpAsTestUser() {
        val username = usernameEditText.text.toString()
        val email = "$username@example.com"
        val password = ""

        usernameEditText.setText(username)
        emailEditText.setText(email)
        passwordEditText.setText(password)
        confirmPasswordEditText.setText(password)
    }

    private fun updateView(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.INVISIBLE
        }
    }

    private fun validationResult(validationResult: Int) {
        var message = ""

        when (validationResult) {
            ValidationObject.EMPTY_VALUES -> {
                message = getString(R.string.enter_all_required_values)
            }
            ValidationObject.PASSWORD_TOO_SHORT -> {
                message = getString(R.string.password_too_short)
            }
            ValidationObject.INVALID_EMAIL -> {
                message = getString(R.string.your_email_address_is_invalid)
            }
            ValidationObject.INCOMPATIBLE_PASSWORDS -> {
                message = getString(R.string.incompatible_passwords)
            }
        }
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

}