package com.example.myentertainment.data

import java.util.*

class Date(var year: Int? = null, var month: Int? = null, var day: Int? = null) {

    fun getMonthFullName(): String? {
        return when (month) {
            0 -> "January"
            1 -> "February"
            2 -> "March"
            3 -> "April"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "August"
            8 -> "September"
            9 -> "October"
            10 -> "November"
            11 -> "December"
            else -> null
        }
    }

    fun getMonthLength(): Int? {
        return when (month) {
            0 -> 31
            1 -> if (year != null) {
                if (year!! % 4 == 0) return 29 else return 28
            } else return null
            2 -> 31
            3 -> 30
            4 -> 31
            5 -> 30
            6 -> 31
            7 -> 31
            8 -> 30
            9 -> 31
            10 -> 30
            11 -> 31
            else -> null
        }
    }

    fun getMonthShortName(): String? {
        return when (month) {
            0 -> "Jan"
            1 -> "Feb"
            2 -> "Mar"
            3 -> "Apr"
            4 -> "May"
            5 -> "Jun"
            6 -> "Jul"
            7 -> "Aug"
            8 -> "Sep"
            9 -> "Oct"
            10 -> "Nov"
            11 -> "Dec"
            else -> null
        }
    }

    fun getUserAge(): Int? {
        if (isDateCorrect()) {
            val birthdayPast: Boolean
            val today = GregorianCalendar()
            val birthDate = GregorianCalendar(year!!, month!!, day!!)

            if (today.get(Calendar.MONTH) > birthDate.get(Calendar.MONTH)) {
                birthdayPast = true

            } else if (today.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH)) {
                birthdayPast = false

            } else {
                birthdayPast =
                    today.get(Calendar.DAY_OF_MONTH) >= birthDate.get(Calendar.DAY_OF_MONTH)
            }

            val age: Int = if (birthdayPast) today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
            else today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR) - 1

            return age

        } else return null
    }

    fun isDateCorrect(): Boolean {
        var result = false

        if (year != null && month != null && day != null) {

            val actualYear = GregorianCalendar().get(Calendar.YEAR)
            if ((year!! in 1900..actualYear) &&
                (month!! in 1..12) &&
                (day!! in 1..getMonthLength()!!)) {
                result = true
            }
        }

        return result
    }

}