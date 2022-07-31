package com.example.myentertainment.view.authentication

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
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.viewmodel.authentication.ChangePasswordViewModel

class ChangePasswordFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var viewModel: ChangePasswordViewModel

    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var loadingSection: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentView = inflater.inflate(R.layout.fragment_change_password, container, false)
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        initView()
        setObservers()
        return fragmentView
    }

    private fun initView() {
        currentPasswordEditText = fragmentView.findViewById(R.id.changePassword_currentPassword)
        newPasswordEditText = fragmentView.findViewById(R.id.changePassword_newPassword)
        confirmPasswordEditText = fragmentView.findViewById(R.id.changePassword_confirmPassword)
        saveButton = fragmentView.findViewById(R.id.changePassword_buttonSave)
        cancelButton = fragmentView.findViewById(R.id.changePassword_buttonCancel)
        loadingSection = fragmentView.findViewById(R.id.changePassword_loadingSection)

        saveButton.setOnClickListener() {
            val currentPassword = currentPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            viewModel.changePassword(currentPassword, newPassword, confirmPassword)
        }

        cancelButton.setOnClickListener() {
            activity!!.finish()
        }
    }

    private fun passwordChangeResult(result: Boolean) {
        var message = ""

        if (result) {
            message = getString(R.string.password_updated)
            activity!!.finish()
        } else {
            message = getString(R.string.something_went_wrong)
        }

        Toast.makeText(activity!!.applicationContext, message, Toast.LENGTH_LONG).show()
    }

    private fun setObservers() {
        viewModel.loading.observe(this, { updateView(it) })
        viewModel.validationResult.observe(this, { validationResult(it) })
        viewModel.changePasswordStatus.observe(this, { passwordChangeResult(it) })
    }

    private fun updateView(loading: Boolean) {
        if (loading) {
            loadingSection.visibility = View.VISIBLE
        } else {
            loadingSection.visibility = View.GONE
        }
    }

    private fun validationResult(validationResult: ValidationResult) {
        var message = ""

        when (validationResult) {
            ValidationResult.EMPTY_VALUES -> message = getString(R.string.enter_all_required_values)
            ValidationResult.PASSWORD_TOO_SHORT -> message = getString(R.string.password_too_short)
            ValidationResult.INCOMPATIBLE_PASSWORDS -> message = getString(R.string.incompatible_passwords)
            ValidationResult.AUTHENTICATION_FAILED -> message = getString(R.string.authentication_failed)
        }

        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

}