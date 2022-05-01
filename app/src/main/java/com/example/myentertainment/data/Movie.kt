package com.example.myentertainment.data

data class Movie(
    var id: String? = null,
    val title: String? = null,
    val releaseYear: String? = null,
    val genre: String? = null,
    val director: String? = null,
    val rating: Float? = null
)