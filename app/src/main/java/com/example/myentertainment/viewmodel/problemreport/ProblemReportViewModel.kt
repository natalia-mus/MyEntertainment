package com.example.myentertainment.viewmodel.problemreport

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.Constants
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.data.Date
import com.example.myentertainment.data.ProblemReport
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class ProblemReportViewModel : ViewModel() {

    private var user: String = Constants.UNKNOWN_USER

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        setUser()
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("reportsReference")
    lateinit var databaseReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()
    val validationResult = MutableLiveData<ValidationResult>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    fun addToDatabase(summary: String, description: String) {
        loading.value = true

        if (validation(summary, description)) {
            val itemId = UUID.randomUUID().toString()
            val report = ProblemReport(itemId, user, getDeviceModel(), getDeviceManufacturer(), getAndroidVersion(), summary, description, getDate())

            databaseReference.child(itemId).setValue(report).addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    loading.value = false
                    addingToDatabaseResult.value = true
                } else {
                    loading.value = false
                    addingToDatabaseResult.value = false
                }
            }
        }
    }

    private fun getAndroidVersion(): String {
        return Build.VERSION.SDK_INT.toString()
    }

    private fun getDeviceManufacturer(): String {
        return Build.MANUFACTURER
    }

    private fun getDeviceModel(): String {
        return Build.MODEL
    }

    private fun getDate(): Date {
        val currentDateTime = java.util.Date()
        val date = Date(currentDateTime)
        date.setTime(currentDateTime)
        return date
    }

    private fun setUser() {
        user = databaseAuth.uid.toString()

        if (user == Constants.NULL) {
            user = Constants.UNKNOWN_USER
        }
    }

    private fun validation(summary: String, description: String): Boolean {
        return if (summary.isEmpty() || description.isEmpty()) {
            loading.value = false
            validationResult.value = ValidationResult.EMPTY_VALUES
            false

        } else true
    }

}