package com.example.myentertainment.viewmodel.authentication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.Constants
import com.example.myentertainment.`object`.ValidationObject
import com.example.myentertainment.data.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class SignUpFragmentViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    val validationResult = MutableLiveData<Int>()
    val loading = MutableLiveData<Boolean>()
    val signingUpStatus = MutableLiveData<Boolean>()

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    fun signUp(username: String, email: String, password: String) {
        loading.value = true

        if (validation(username, email, password)) {
            databaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        createUserProfile(username, email)
                    } else {
                        loading.value = false
                        signingUpStatus.value = false
                    }
                }
        }

    }

    private fun createUserProfile(username: String, email: String) {
        val user = databaseAuth.uid.toString()
        val userProfileData = UserProfile(username, null, null, null, null, email)

        databaseReference.child(user).child(Constants.USER_PROFILE_DATA).setValue(userProfileData)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    loading.value = false
                    signingUpStatus.value = true
                } else {
                    loading.value = false
                    signingUpStatus.value = false
                }
            }
    }

    private fun validation(username: String, email: String, password: String): Boolean {

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            loading.value = false
            validationResult.value = ValidationObject.EMPTY_VALUES
            return false
        } else if (password.length < 6) {
            loading.value = false
            validationResult.value = ValidationObject.PASSWORD_TOO_SHORT
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loading.value = false
            validationResult.value = ValidationObject.INVALID_EMAIL
            return false
        } else {
            return true
        }
    }
}