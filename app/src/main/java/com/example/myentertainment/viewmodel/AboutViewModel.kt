package com.example.myentertainment.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class AboutViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    @Named("aboutReference")
    lateinit var databaseReference: DatabaseReference

    fun getDescription() {
        databaseReference.get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // todo
            } else {
                // todo
            }
        }
    }

}