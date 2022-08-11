package com.openclassrooms.realestatemanager.utils

import org.junit.Assert
import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UtilsTests {

    @Test
    fun convertDollarToEuroTest() {
        val value = 100
        val expectedValue = 98
        Assert.assertEquals(expectedValue, Utils.convertDollarToEuro(value))
    }

    @Test
    fun convertEuroToDollarTest() {
        val value = 98
        val expectedValue = 100
        Assert.assertEquals(expectedValue, Utils.convertEuroToDollar(value))
    }

    @Test
    fun todayDateTest() {
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        val todayDate = dateFormat.format(Date())

        Assert.assertEquals(todayDate, Utils.todayDate)
    }

    @Test
    fun convertDateToStringTest() {
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val expectedDate = "06/08/2022"
        val date = formatter.parse(expectedDate)

        val result = Utils.convertDateToString(date!!)

        Assert.assertEquals(result, expectedDate)
    }
}