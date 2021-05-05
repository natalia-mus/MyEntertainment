package com.example.myentertainment.view.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myentertainment.R
import com.example.myentertainment.viewmodel.SignUpFragmentViewModel

class SignUpFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewModel: SignUpFragmentViewModel

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var signUpButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_sign_up, container, false)
        viewModel = ViewModelProvider(this).get(SignUpFragmentViewModel::class.java)

        val emailLabel = fragmentView.findViewById<EditText>(R.id.signUp_email)
        val passwordLabel = fragmentView.findViewById<EditText>(R.id.signUp_password)
        signUpButton = fragmentView.findViewById(R.id.signUp_buttonOk)

        signUpButton.setOnClickListener() {
            email = emailLabel.text.toString()
            password = passwordLabel.text.toString()
            viewModel.signUp(email, password)
        }

        return fragmentView
    }
}