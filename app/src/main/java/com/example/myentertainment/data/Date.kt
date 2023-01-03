package com.example.myentertainment.data

import android.annotation.SuppressLint
import com.google.firebase.database.Exclude
import java.text.SimpleDateFormat
import java.util.*

class Date {

    var year: Int? = null
    var month: Int? = null
    var day: Int? = null

    var hours: Int? = null
    var minutes: Int? = null
    var seconds: Int? = null


    // non-argument constructor required by Firebase
    @Suppress("unused")
    constructor() {}

    constructor(year: Int? = null, month: Int? = null, day: Int? = null) {
        this.year = year
        this.month = month
        this.day = day
    }

    @SuppressLint("SimpleDateFormat")
    constructor(date: java.util.Date) {
        var simpleDateFormat = SimpleDateFormat("yyyy")
        val year = simpleDateFormat.format(date)

        simpleDateFormat = SimpleDateFormat("M")
        val month = simpleDateFormat.format(date)

        simpleDateFormat = SimpleDateFormat("dd")
        val day = simpleDateFormat.format(date)

        this.year = year.toInt()
        this.month = month.toInt() - 1
        this.day = day.toInt()
    }

    @SuppressLint("SimpleDateFormat")
    fun setTime(time: java.util.Date) {
        var simpleDateFormat = SimpleDateFormat("H")
        val hours = simpleDateFormat.format(time)

        simpleDateFormat = SimpleDateFormat("m")
        val minutes = simpleDateFormat.format(time)

        simpleDateFormat = SimpleDateFormat("s")
        val seconds = simpleDateFormat.format(time)

        this.hours = hours.toInt()
        this.minutes = minutes.toInt()
        this.seconds = seconds.toInt()
    }

    fun setTime(hours: Int, minutes: Int, seconds: Int) {
        this.hours = hours
        this.minutes = minutes
        this.seconds = seconds
    }

    @Exclude
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

    @Exclude
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

    @Exclude
    fun getUserAge(): Int? {
        if (year != null && month != null && day != null) {
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

}