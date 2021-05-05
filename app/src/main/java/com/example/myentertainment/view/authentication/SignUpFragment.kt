package com.example.myentertainment.view.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
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
    }

    private fun updateView(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.INVISIBLE
        }
    }
}