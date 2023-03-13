package com.example.myentertainment

import com.example.myentertainment.data.Date
import org.junit.Assert.assertEquals
import org.junit.Test

class DateTest {

    private val correctDate = Date(1993, 2, 12)
    private val incorrectDate = Date(1993, 15, 35)
    private val nullDate = Date(null, null, null)


    // getMonthFullName:
    @Test
    fun monthFullNameWhenCorrectMonthValue() {
        assertEquals("March", correctDate.getMonthFullName())
    }

    @Test
    fun monthFullNameWhenIncorrectMonthValue() {
        assertEquals(null, incorrectDate.getMonthFullName())
    }

    @Test
    fun monthFullNameWhenNullMonthValue() {
        assertEquals(null, nullDate.getMonthFullName())
    }


    // getMonthShortName
    @Test
    fun monthShortNameWhenCorrectMonthValue() {
        assertEquals("Mar", correctDate.getMonthShortName())
    }

    @Test
    fun monthShortNameWhenIncorrectMonthValue() {
        assertEquals(null, incorrectDate.getMonthShortName())
    }

    @Test
    fun monthShortNameWhenNullMonthValue() {
        assertEquals(null, nullDate.getMonthShortName())
    }


    // getUserAge
    @Test
    fun userAgeWhenCorrectDate() {
        assertEquals(30, correctDate.getUserAge())
    }

    @Test
    fun userAgeWhenIncorrectDate() {
        assertEquals(null, incorrectDate.getUserAge())
    }

    @Test
    fun userAgeWhenNullDate() {
        assertEquals(null, nullDate.getUserAge())
    }

    @Test
    fun userAgeWhenNullYear() {
        val date = Date(null, 3, 27)
        assertEquals(null, date.getUserAge())
    }

    @Test
    fun userAgeWhenNullMonth() {
        val date = Date(1992, null, 27)
        assertEquals(null, date.getUserAge())
    }

    @Test
    fun userAgeWhenNullDay() {
        val date = Date(1992, 3, null)
        assertEquals(null, date.getUserAge())
    }
}