package com.example.myentertainment.viewmodel.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.ValidationResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class ChangePasswordViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    val validationResult = MutableLiveData<ValidationResult>()
    val loading = MutableLiveData<Boolean>()
    val changePasswordResult = MutableLiveData<Boolean>()

    private val currentUser = databaseAuth.currentUser!!


    fun changePassword(currentPassword: String, newPassword: String, confirmPassword: String) {
        loading.value = true

        if (validation(currentPassword, newPassword, confirmPassword)) {
            val email = currentUser.email!!
            val credential = EmailAuthProvider.getCredential(email, currentPassword)

            currentUser.reauthenticate(credential)
                .addOnSuccessListener {
                    updatePassword(newPassword)
                }
                .addOnFailureListener {
                    loading.value = false
                    validationResult.value = ValidationResult.AUTHENTICATION_FAILED
                }
        }
    }

    private fun updatePassword(newPassword: String) {
        currentUser.updatePassword(newPassword)
            .addOnSuccessListener {
                loading.value = false
                changePasswordResult.value = true
            }
            .addOnFailureListener {
                loading.value = false
                changePasswordResult.value = false
            }
    }

    private fun validation(currentPassword: String, newPassword: String, confirmPassword: String): Boolean {
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            loading.value = false
            validationResult.value = ValidationResult.EMPTY_VALUES
            return false

        } else if (newPassword.length < 6) {
            loading.value = false
            validationResult.value = ValidationResult.PASSWORD_TOO_SHORT
            return false

        } else if (newPassword != confirmPassword) {
            loading.value = false
            validationResult.value = ValidationResult.INCOMPATIBLE_PASSWORDS
            return false

        } else {
            return true
        }
    }

}