package com.example.myentertainment.viewmodel.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.data.Music
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class AddMusicFragmentViewModel : ViewModel() {

    private val user: String
    private val path: DatabaseReference

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        path = entertainmentReference.child(user).child(CategoryObject.MUSIC.categoryName)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("entertainmentReference")
    lateinit var entertainmentReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()
    val validationResult = MutableLiveData<ValidationResult>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    fun addToDatabase(item: Music) {
        loading.value = true

        val itemId = UUID.randomUUID().toString()
        val title = item.title
        val artist = item.artist
        val releaseYear = item.releaseYear
        val genre = item.genre
        val rating = item.rating

        val music = Music(itemId, title, artist, releaseYear, genre, rating)

        if (validation(music)) {
            path.child(itemId).setValue(music)
                .addOnCompleteListener { task ->
                    loading.value = false
                    addingToDatabaseResult.value = task.isSuccessful
                }
        }
    }

    fun updateItem(item: Music) {
        loading.value = true

        if (validation(item)) {
            val song = hashMapOf<String, Any>(item.id.toString() to item)
            path.updateChildren(song).addOnCompleteListener { task ->
                loading.value = false
                addingToDatabaseResult.value = task.isSuccessful
            }
        }
    }

    private fun validation(music: Music): Boolean {
        return if (music.title.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationResult.EMPTY_VALUES
            false

        } else true
    }

}