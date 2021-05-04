package com.example.myentertainment.view.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myentertainment.R

class SignInFragment(private val onSignUpClickAction: OnSignUpClickAction) : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var signInButton: Button
    private lateinit var signUp: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_sign_in, container, false)
        signInButton = fragmentView.findViewById(R.id.signIn_buttonOk)
        signUp = fragmentView.findViewById(R.id.signIn_signUp)

        signUp.setOnClickListener() {
            onSignUpClickAction.signUpClicked()
        }

        return fragmentView
    }
}


interface OnSignUpClickAction {
    fun signUpClicked()
}