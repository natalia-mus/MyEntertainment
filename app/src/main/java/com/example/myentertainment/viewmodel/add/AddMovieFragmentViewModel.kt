package com.example.myentertainment.viewmodel.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.Constants
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.`object`.ValidationResult
import com.example.myentertainment.data.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class AddMovieFragmentViewModel : ViewModel() {

    private val user: String
    private val mainPath: DatabaseReference
    private var itemId: String = "0"

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        mainPath = databaseReference.child(user).child(CategoryObject.MOVIES)
        setItemId()
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    val loading = MutableLiveData<Boolean>()
    val movie = MutableLiveData<Movie>()
    val validationResult = MutableLiveData<ValidationResult>()
    val addingToDatabaseResult = MutableLiveData<Boolean>()


    fun addToDatabase(item: Movie) {
        loading.value = true

        val title = item.title
        val releaseYear = item.releaseYear
        val genre = item.genre
        val director = item.director
        val rating = item.rating

        val movie = Movie(itemId, title, releaseYear, genre, director, rating)

        if (validation(movie)) {
            mainPath.child(itemId).setValue(movie)
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

    fun getMovie(id: String) {
        databaseReference.child(user).child(CategoryObject.MOVIES).get().addOnSuccessListener {
            movie.value = it.child(id).getValue(Movie::class.java)
        }
    }

    fun updateItem(item: Movie) {
        loading.value = true

        if (validation(item)) {
            val movie = hashMapOf<String, Any>(item.id.toString() to item)
            mainPath.updateChildren(movie).addOnCompleteListener() { task ->
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

    private fun validation(movie: Movie): Boolean {
        return if (movie.title.isNullOrEmpty()) {
            loading.value = false
            validationResult.value = ValidationResult.EMPTY_VALUES
            false

        } else true
    }

}