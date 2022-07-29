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
import com.example.myentertainment.viewmodel.authentication.ChangePasswordViewModel

class ChangePasswordFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewModel: ChangePasswordViewModel

    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_change_password, container, false)
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        initView()
        return fragmentView
    }

    private fun initView() {
        currentPasswordEditText = fragmentView.findViewById(R.id.changePassword_currentPassword)
        newPasswordEditText = fragmentView.findViewById(R.id.changePassword_newPassword)
        confirmPasswordEditText = fragmentView.findViewById(R.id.changePassword_confirmPassword)
        saveButton = fragmentView.findViewById(R.id.changePassword_buttonSave)
        cancelButton = fragmentView.findViewById(R.id.changePassword_buttonCancel)

        saveButton.setOnClickListener() {
            val currentPassword = currentPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            viewModel.changePassword(currentPassword, newPassword, confirmPassword)
        }

        cancelButton.setOnClickListener() {
            activity?.finish()
        }
    }
}