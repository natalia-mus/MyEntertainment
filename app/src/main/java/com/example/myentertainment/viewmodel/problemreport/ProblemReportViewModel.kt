package com.example.myentertainment.viewmodel.problemreport

import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.ProblemReport
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class ProblemReportViewModel : ViewModel() {

    private val mainPath: DatabaseReference

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("problemsReference")
    lateinit var databaseReference: DatabaseReference

    fun addToDatabase(report: ProblemReport) {

    }
}