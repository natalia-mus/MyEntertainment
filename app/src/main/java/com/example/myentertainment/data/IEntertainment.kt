package com.example.myentertainment.data

import java.sql.Timestamp

interface IEntertainment {

    var creationDate: String?

    fun getCurrentDate(currentDate: String?): String {
        return if (creationDate != null) creationDate!! else Timestamp(System.currentTimeMillis()).toString()
    }
}