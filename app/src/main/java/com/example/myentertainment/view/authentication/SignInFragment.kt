package com.example.myentertainment.view.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.view.main.MainActivity
import com.example.myentertainment.viewmodel.SignInFragmentViewModel

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

        signUp.setOnClickListener() {
            onSignUpClickAction.signUpClicked()
        }
    }

    private fun setObservers() {
        viewModel.userId.observe(this, { updateView(it) })
    }

    private fun updateView(userId: String?) {
        if (userId != "null") {
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            loadingSection.visibility = View.INVISIBLE
            signInSection.visibility = View.VISIBLE
        }
    }
}


interface OnSignUpClickAction {
    fun signUpClicked()
}