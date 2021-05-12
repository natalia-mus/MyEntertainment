package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.ValidationObject
import com.example.myentertainment.data.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class AddBookFragmentViewModel : ViewModel() {

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


    fun addToDatabase(book: Book) {
        loading.value = true
        if (validation(book)) {
            databaseReference.child(user).child("books").child(itemId).setValue(book)
                .addOnCompleteListener() { task ->
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

    private fun setItemId() {
        databaseReference.child(user).child("books")
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

    private fun validation(book: Book): Boolean {
        return if (book.title.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationObject.EMPTY_VALUES
            false
        } else true
    }
}