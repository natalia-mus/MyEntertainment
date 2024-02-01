package com.example.myentertainment.viewmodel.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.data.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class AddMovieFragmentViewModel : ViewModel() {

    private val user: String
    private val path: DatabaseReference

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        path = entertainmentReference.child(user).child(CategoryObject.MOVIES.categoryName)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("entertainmentReference")
    lateinit var entertainmentReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()
    val validationResult = MutableLiveData<ValidationResult>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    fun addToDatabase(item: Movie) {
        loading.value = true

        val itemId = UUID.randomUUID().toString()
        val title = item.title
        val releaseYear = item.releaseYear
        val genre = item.genre
        val director = item.director
        val rating = item.rating

        val movie = Movie(itemId, title, releaseYear, genre, director, rating)

        if (validation(movie)) {
            path.child(itemId).setValue(movie)
                .addOnCompleteListener { task ->
                    loading.value = false
                    addingToDatabaseResult.value = task.isSuccessful
                }
        }
    }

    fun updateItem(item: Movie) {
        loading.value = true

        if (validation(item)) {
            val movie = hashMapOf<String, Any>(item.id.toString() to item)
            path.updateChildren(movie).addOnCompleteListener { task ->
                loading.value = false
                addingToDatabaseResult.value = task.isSuccessful
            }
        }
    }

    private fun validation(movie: Movie): Boolean {
        return if (movie.title.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationResult.EMPTY_VALUES
            false

        } else true
    }

}