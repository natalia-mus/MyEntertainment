package com.example.myentertainment.viewmodel.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.Constants
import com.example.myentertainment.`object`.CategoryObject
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
    private val mainPath: DatabaseReference
    private var itemId: String = "0"

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        mainPath = databaseReference.child(user).child(CategoryObject.MUSIC)
        setItemId()
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()
    val song = MutableLiveData<Music>()
    val validationResult = MutableLiveData<Int>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    fun getSong(id: String) {
        databaseReference.child(user).child(CategoryObject.MUSIC).get().addOnSuccessListener {
            song.value = it.child(id).getValue(Music::class.java)
        }
    }

    fun addToDatabase(item: Music) {
        loading.value = true

        val title = item.title
        val artist = item.artist
        val releaseYear = item.releaseYear
        val genre = item.genre
        val rating = item.rating

        val music = Music(itemId, title, artist, releaseYear, genre, rating)

        if (validation(music)) {
            mainPath.child(itemId).setValue(music)
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

    fun updateItem(item: Music) {
        loading.value = true

        if (validation(item)) {
            val song = hashMapOf<String, Any>(item.id.toString() to item)
            mainPath.updateChildren(song).addOnCompleteListener() { task ->
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
        mainPath
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

    private fun validation(music: Music): Boolean {
        return if (music.title.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationObject.EMPTY_VALUES
            false
        } else true
    }
}