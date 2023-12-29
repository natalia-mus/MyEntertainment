package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.About
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

    val fetchingDataStatus = MutableLiveData<Boolean>()
    val about = MutableLiveData<About>()

    fun getData() {
        databaseReference.get()
            .addOnSuccessListener {
                about.value = it.getValue(About::class.java)
                fetchingDataStatus.value = true

            }.addOnFailureListener {
                fetchingDataStatus.value = false
            }
    }

}