package com.example.myentertainment.viewmodel.problemreport

import android.util.Log
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

    private val user: String
    private var itemId: String = "0"

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        setItemId()
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("reportsReference")
    lateinit var databaseReference: DatabaseReference

    fun addToDatabase(item: ProblemReport) {
        val report = ProblemReport(itemId, user, item.summary, item.description)

        databaseReference.child(itemId).setValue(report).addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                Log.e("ProblemReportViewModel", "success")
            } else {
                Log.e("ProblemReportViewModel", "failure")
            }
        }
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
}