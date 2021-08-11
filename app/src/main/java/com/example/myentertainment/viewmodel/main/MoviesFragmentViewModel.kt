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

class MoviesFragmentViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseAuth: FirebaseAuth

    @Inject
    lateinit var databaseReference: DatabaseReference

    private val user = databaseAuth.uid.toString()

    val movies = MutableLiveData<List<Movie>>()

    fun fetchMovies() {
        databaseReference.child(user).child(CategoryObject.MOVIES).get().addOnSuccessListener {
            val moviesList: MutableList<Movie> = mutableListOf()
            var lastChild = it.childrenCount

            var i = 0
            while (i < lastChild) {
                if (!it.child(i.toString()).exists()) {
                    lastChild++
                }
                i++
            }

            for (j in 0 until lastChild) {
                val singleMovie = it.child(j.toString()).getValue(Movie::class.java)
                singleMovie?.let { movie -> moviesList.add(movie) }
            }

            movies.value = moviesList
        }
    }


    fun deleteMovie(id: String?) {
        databaseReference.child(user).child(CategoryObject.MOVIES).child(id.toString())
            .removeValue()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    fetchMovies()
                } else {
                    Log.e("error", "error")
                }
            }
    }
}