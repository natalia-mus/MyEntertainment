package com.example.myentertainment.viewmodel.authentication

import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication

class ChangePasswordViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        // TODO
    }
}