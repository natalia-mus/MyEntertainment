package com.example.myentertainment.viewmodel.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.ValidationResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SignInFragmentViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    val userId = MutableLiveData<String>()
    val validationResult = MutableLiveData<ValidationResult>()
    val loading = MutableLiveData<Boolean>()
    val signingInStatus = MutableLiveData<Boolean>()


    fun checkUserId() {
        userId.value = databaseAuth.uid.toString()
    }

    fun signIn(email: String, password: String) {
        loading.value = true

        if (validation(email, password)) {
            databaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isComplete) {
                        loading.value = false
                        signingInStatus.value = task.isSuccessful
                    } else {
                        loading.value = false
                        signingInStatus.value = false
                    }
                }
        }

    }

    private fun validation(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            loading.value = false
            validationResult.value = ValidationResult.EMPTY_VALUES
            return false

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.value = false
            validationResult.value = ValidationResult.INVALID_EMAIL
            return false

        } else {
            return true
        }
    }

}