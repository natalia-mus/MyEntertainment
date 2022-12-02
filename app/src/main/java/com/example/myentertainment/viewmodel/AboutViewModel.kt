package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
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

    val loading = MutableLiveData<Boolean>()
    val description = MutableLiveData<String>()

    fun getDescription() {
        loading.value = true
        databaseReference.get().addOnCompleteListener() { task ->
            if (task.isSuccessful && task.result != null) {
                loading.value = false
                description.value = task.result!!.value.toString()
            } else {
                loading.value = false
            }
        }
    }

}