package com.example.myentertainment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class SignUpFragmentViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var database: DatabaseReference

    fun signUp(email: String, password: String) {
        Log.e("viewModel database", database.toString())
    }
}