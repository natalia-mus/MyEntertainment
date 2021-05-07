package com.example.myentertainment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myentertainment.BaseApplication
import com.example.myentertainment.data.Movie
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class AddMovieFragmentViewModel : ViewModel() {

    init {
        BaseApplication.baseApplicationComponent.inject(this)
    }

    @Inject
    lateinit var databaseReference: DatabaseReference

    fun addToDatabase(movie: Movie) {
        Log.e("database", databaseReference.toString())
        //databaseReference.child("movies").setValue(movie)
    }
}