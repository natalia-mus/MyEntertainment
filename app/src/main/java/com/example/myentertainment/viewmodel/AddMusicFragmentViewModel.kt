package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.ValidationObject
import com.example.myentertainment.data.Music
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class AddMusicFragmentViewModel : ViewModel() {

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

    val loading = MutableLiveData<Boolean>()
    val validationResult = MutableLiveData<Int>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()

    fun addToDatabase(music: Music) {
        loading.value = true
        if (validation(music)) {
            databaseReference.child(user).child("music").child(itemId).setValue(music)
                .addOnCompleteListener() { task ->
                    if (task.isComplete) {
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
    }

    private fun setItemId() {
        databaseReference.child(user).child("music")
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

    private fun validation(music: Music): Boolean {
        return if (music.title.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationObject.EMPTY_VALUES
            false
        } else true
    }
}