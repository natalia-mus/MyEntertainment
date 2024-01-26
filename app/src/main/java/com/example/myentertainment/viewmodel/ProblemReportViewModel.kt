package com.example.myentertainment.viewmodel

import android.net.Uri
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.Constants
import com.example.myentertainment.`object`.StoragePathObject
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.data.Date
import com.example.myentertainment.data.ProblemReport
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
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

    @Inject
    lateinit var storageReference: StorageReference

    val loading = MutableLiveData<Boolean>()
    val validationResult = MutableLiveData<ValidationResult>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    fun addToDatabase(summary: String, description: String, screenshots: HashMap<UUID, Uri>) {
        loading.value = true

        if (validation(summary, description)) {
            val itemId = UUID.randomUUID().toString()
            val screenshotsIds = addScreenshotsToDatabase(screenshots)
            val report = ProblemReport(itemId, user, getDeviceModel(), getDeviceManufacturer(), getAndroidVersion(), summary, description, screenshotsIds, getDate())

            databaseReference.child(itemId).setValue(report).addOnCompleteListener { task ->
                loading.value = false
                addingToDatabaseResult.value = task.isSuccessful
            }
        }
    }

    /**
     * Adds screenshots to database storage
     * Returns ArrayList of screenshots UUIDs
     */
    private fun addScreenshotsToDatabase(screenshots: HashMap<UUID, Uri>): ArrayList<String> {
        val ids = ArrayList<String>()

        for (screenshot in screenshots) {
            val uuid = screenshot.key
            val file = screenshot.value
            getStorageReference(uuid).putFile(file)
            ids.add(uuid.toString())
        }

        return ids
    }

    private fun getAndroidVersion(): String {
        return Build.VERSION.SDK_INT.toString()
    }

    private fun getDate(): Date {
        val currentDateTime = java.util.Date()
        val date = Date(currentDateTime)
        date.setTime(currentDateTime)
        return date
    }

    private fun getDeviceManufacturer(): String {
        return Build.MANUFACTURER
    }

    private fun getDeviceModel(): String {
        return Build.MODEL
    }

    private fun getStorageReference(uuid: UUID) : StorageReference {
        val path = StoragePathObject.PATH_REPORTS + "/" + uuid
        return storageReference.child(path)
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