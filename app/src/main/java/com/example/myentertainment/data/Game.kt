package com.example.myentertainment.data

data class Game(
    var id: String? = null,
    val title: String? = null,
    val releaseYear: String? = null,
    val genre: String? = null,
    val rating: Float? = null,
    override var creationDate: String? = null
) : IEntertainment {

    init {
        creationDate = getCurrentDate(creationDate)
    }
}