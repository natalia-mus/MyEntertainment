package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class MainActivityViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth


    fun signOut() {
        databaseAuth.signOut()
    }

}