package com.example.myentertainment.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    var id: String? = null,
    var title: String? = null,
    var releaseYear: String? = null,
    var genre: String? = null,
    var rating: Float? = null,
    override var creationDate: String? = null
) : IEntertainment, Parcelable {

    init {
        creationDate = getCurrentDate(creationDate)
    }
}