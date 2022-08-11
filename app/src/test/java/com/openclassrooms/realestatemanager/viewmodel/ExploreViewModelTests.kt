package com.openclassrooms.realestatemanager.viewmodel

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Type
import io.reactivex.rxjava3.core.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ExploreViewModelTests {

    private lateinit var viewModel: ExploreViewModel

    private val propertyDao : PropertyDao = mock()

    @Before
    fun setup() {
        viewModel = ExploreViewModel(propertyDao)
    }

    @Test
    fun loadAllAddressAreaTest() {
        val expectedResult = listOf<String>()

        whenever(propertyDao.loadAllAddressArea())
            .thenReturn(Single.just(expectedResult))

        viewModel.loadAllAddressArea()
            .test()
            .assertValue(expectedResult)

        verify(propertyDao).loadAllAddressArea()
    }

    @Test
    fun getQueryForSelectTest() {
        val entryValue = listOf(Type.CASTLE, Type.HOUSE)
        val expectedValue = "(\"CASTLE\",\"HOUSE\")"

        val testValue = viewModel.getQueryForSelect(entryValue)

        Assert.assertEquals(testValue, expectedValue)
    }

    @Test
    fun getQueryForSelectOptionsTest() {
        val entryValue = listOf(Option.CLOSE_TO_SHOPS, Option.AIR_CONDITIONER)
        val expectedValue = "\"%CLOSE_TO_SHOPS%\" OR options LIKE \"%AIR_CONDITIONER%\""

        val testValue = viewModel.getQueryForSelectOptions(entryValue)

        Assert.assertEquals(testValue, expectedValue)
    }

    @Test
    fun applyAllFiltersTest() {
        //TODO
    }
}