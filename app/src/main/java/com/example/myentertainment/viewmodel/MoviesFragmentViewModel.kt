package com.example.myentertainment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
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
        databaseReference.child(user).child("movies").get().addOnSuccessListener {
            val countItems = it.childrenCount
            val moviesList: MutableList<Movie> = mutableListOf()

            for (i in 0 until countItems) {
                val singleMovie = it.child(i.toString()).getValue(Movie::class.java)
                moviesList.add(singleMovie!!)
            }
            movies.value = moviesList
        }
    }
}