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

    private val mainPath: DatabaseReference
    private val user: String

    init {
        BaseApplication.baseApplicationComponent.inject(this)
        user = databaseAuth.uid.toString()
        mainPath = entertainmentReference.child(user).child(CategoryObject.MOVIES)
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
        mainPath.child(id.toString())
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
        mainPath.get().addOnSuccessListener {
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

}