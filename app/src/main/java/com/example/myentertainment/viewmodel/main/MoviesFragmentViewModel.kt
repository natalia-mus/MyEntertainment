package com.example.myentertainment.viewmodel.main

import androidx.lifecycle.MutableLiveData
import com.example.myentertainment.data.Movie

class MoviesFragmentViewModel : EntertainmentViewModel() {

    val movies = MutableLiveData<List<Movie>>()
    //val itemDeleted = MutableLiveData<Boolean>(false)


    fun deleteMovie(id: String?) {
        itemDeleted.value = false
//        path.child(id.toString())
//            .removeValue()
//            .addOnCompleteListener() { task ->
//                if (task.isSuccessful) {
//                    fetchMovies()
//                    itemDeleted.value = true
//                } else {
//                    Log.e("MoviesFragmentViewModel", "error")
//                }
//            }
    }

    fun fetchMovies() {
//        val result = ArrayList<IEntertainment>()
//
//        path.get().addOnSuccessListener {
//            it.children.forEach {
//                val child = it.getValue(Movie::class.java)
//                child?.let { movie -> result.add(movie) }
//            }
//
//            movies.value = orderByCreationDate(result) as ArrayList<Movie>
//        }

        fetchItems()
    }

    override fun onItemsValueChanged() {
        movies.value = entertainmentList.value as ArrayList<Movie>
    }

}