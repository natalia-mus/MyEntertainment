package com.example.myentertainment.data

import java.sql.Timestamp

data class Book(
    var id: String? = null,
    val title: String? = null,
    val author: String? = null,
    val releaseYear: String? = null,
    val genre: String? = null,
    val rating: Float? = null,
    val creationDate: String = getCreationDate()
) {

    companion object {
        private fun getCreationDate(): String {
            return Timestamp(System.currentTimeMillis()).toString()
        }
    }
}