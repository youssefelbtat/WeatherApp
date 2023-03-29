package com.example.weatherapp.helper

import com.example.weatherapp.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test



class ConvertorTest {

    private var dt :Long = 0

    @Before
    fun setUp(){
         dt = 1648562400L
    }

    @Test
    fun convertDtToDayTest_Dt_returnDayOfTheWeek() {
        // Given

        val expected = "Tuesday"

        // When
        val result = Convertor.convertDtToDay(dt)

        // Return
        assertEquals(expected, result)
    }

    @Test
    fun convertDtToDateTest_Dt_returnDate() {
        // Given
        //val dt = 1648562400L
        val expected = "Tue, 29 Mar 16:00"

        // When
        val result = Convertor.convertDtToDate(dt)

        // Return
        assertEquals(expected, result)
    }

    @Test
    fun convertDtToTimeTest_Dt_returnTime() {
        // Given
        //val dt = 1648562400L
        val expected = "4:00PM"

        // When
        val result = Convertor.convertDtToTime(dt)

        // Return
        assertEquals(expected, result)
    }

    @Test
    fun convertIconToDrawableImageTest_stringIcon_returnDrawableImageId() {
        // Given
        val icon = "01d"
        val expected = R.drawable.clear_sky

        // When
        val result = Convertor.convertIconToDrawableImage(icon)

        // Return
        assertEquals(expected, result)
    }

}
