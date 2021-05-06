package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SignInFragmentViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    val userId = MutableLiveData<String>()

    fun checkUserId() {
        userId.value = databaseAuth.uid.toString()
    }
}