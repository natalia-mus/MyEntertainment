package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SignUpFragmentViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    val signingUpStatus = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    fun signUp(email: String, password: String) {
        loading.value = true
        databaseAuth.createUserWithEmailAndPassword(email, password)
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
}