package com.example.myentertainment.viewmodel.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.ValidationObject

class ChangePasswordViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    val validationResult = MutableLiveData<Int>()
    val loading = MutableLiveData<Boolean>()

    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        loading.value = true

        if (validation(currentPassword, newPassword, confirmPassword)) {
            // TODO
        }
    }

    private fun validation(currentPassword: String, newPassword: String, confirmPassword: String): Boolean {
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            loading.value = false
            validationResult.value = ValidationObject.EMPTY_VALUES
            return false

        } else if (newPassword.length < 6) {
            loading.value = false
            validationResult.value = ValidationObject.PASSWORD_TOO_SHORT
            return false

        } else if (newPassword != confirmPassword) {
            loading.value = false
            validationResult.value = ValidationObject.INCOMPATIBLE_PASSWORDS
            return false

        } else {
            return true
        }
    }

}