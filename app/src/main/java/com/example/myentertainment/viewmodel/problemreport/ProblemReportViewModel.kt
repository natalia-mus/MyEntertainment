package com.example.myentertainment.viewmodel.problemreport

import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.Constants
import com.example.myentertainment.data.ProblemReport
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import javax.inject.Named

class ProblemReportViewModel : ViewModel() {

    private var user: String = Constants.UNKNOWN_USER
    private var itemId: String = "0"

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        setUser()
        setItemId()
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("reportsReference")
    lateinit var databaseReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()


    fun addToDatabase(summary: String, description: String) {
        loading.value = true
        val report = ProblemReport(itemId, user, getDeviceModel(), getDeviceManufacturer(), getAndroidVersion(), summary, description)

        databaseReference.child(itemId).setValue(report).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                loading.value = false
                Log.e("ProblemReportViewModel", "success")
            } else {
                loading.value = false
                Log.e("ProblemReportViewModel", "failure")
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

    private fun setItemId() {
        databaseReference
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val childrenCount = snapshot.childrenCount
                        itemId = childrenCount.toString()

                        for (i in 0 until childrenCount) {
                            val child = snapshot.child(i.toString()).value
                            if (child.toString() == Constants.NULL) itemId = i.toString()
                        }
                    }
                }
            })
    }

    private fun setUser() {
        user = databaseAuth.uid.toString()

        if (user == Constants.NULL) {
            user = Constants.UNKNOWN_USER
        }
    }

}