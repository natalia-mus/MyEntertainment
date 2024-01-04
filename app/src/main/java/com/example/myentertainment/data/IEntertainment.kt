package com.example.myentertainment.data

import java.sql.Timestamp

interface IEntertainment {

    var creationDate: String?

    fun getCurrentDate(): String {
        return Timestamp(System.currentTimeMillis()).toString()
    }
}