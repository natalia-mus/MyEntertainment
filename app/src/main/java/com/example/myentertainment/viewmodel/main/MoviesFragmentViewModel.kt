package com.example.myentertainment.viewmodel.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.`object`.CategoryObject
import com.example.myentertainment.data.Movie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject
import javax.inject.Named

class MoviesFragmentViewModel : ViewModel() {

    private val path: DatabaseReference
    private val user: String

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        path = entertainmentReference.child(user).child(CategoryObject.MOVIES)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    @Named("entertainmentReference")
    lateinit var entertainmentReference: DatabaseReference

    val movies = MutableLiveData<List<Movie>>()
    val itemDeleted = MutableLiveData<Boolean>(false)


    fun deleteMovie(id: String?) {
        itemDeleted.value = false
        path.child(id.toString())
            .removeValue()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    fetchMovies()
                    itemDeleted.value = true
                } else {
                    Log.e("MoviesFragmentViewModel", "error")
                }
            }
    }

    fun fetchMovies() {
        val result = ArrayList<Movie>()

        path.get().addOnSuccessListener {
            it.children.forEach {
                val child = it.getValue(Movie::class.java)
                child?.let { movie -> result.add(movie) }
            }

            movies.value = orderByCreationDate(result)
        }
    }

    private fun orderByCreationDate(array: ArrayList<Movie>): ArrayList<Movie> {
        array.sortBy { it.creationDate }
        return array
    }

}