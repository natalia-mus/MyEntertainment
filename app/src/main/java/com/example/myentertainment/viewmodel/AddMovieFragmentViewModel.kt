package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class AddMovieFragmentViewModel : ViewModel() {

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
    lateinit var databaseReference: DatabaseReference

    //val addingToDatabaseStatus = MutableLiveData<Boolean>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()

    fun addToDatabase(movie: Movie) {
        databaseReference.child(user).child("movies").child(itemId).setValue(movie)
            .addOnCompleteListener() { task ->
                if (task.isComplete) {
                    if (task.isSuccessful) {
                        //addingToDatabaseStatus.value = false
                        addingToDatabaseResult.value = true
                    } else {
                        //addingToDatabaseStatus.value = false
                        addingToDatabaseResult.value = false
                    }
                }
            }
    }

    private fun setItemId() {
        databaseReference.child(user).child("movies")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val childrenCount = snapshot.childrenCount
                        itemId = childrenCount.toString()

                        for (i in 0 until childrenCount) {
                            val child = snapshot.child(i.toString()).value
                            if (child.toString() == "null") itemId = i.toString()
                        }
                    }
                }
            })
    }

}