package com.openclassrooms.realestatemanager.viewmodel

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class ModifyPropertyViewModelTests {

    private lateinit var viewModel: ModifyPropertyViewModel

    private val propertyDaoMock: PropertyDao = mock()

    private val property : Property = mock()

    @Before
    fun setup() {
        viewModel = ModifyPropertyViewModel(propertyDaoMock)
        MockitoAnnotations.openMocks(property)
    }

    @After
    fun teardown() {
        verifyNoMoreInteractions(propertyDaoMock)
    }

    /*@Test
    fun updateProperty_WithSuccess(){

        whenever(propertyDaoMock.updateProperty(property))
            .thenReturn(Completable.complete())

        viewModel.updateProperty(
            property.ref,
            property.type,
            property.price,
            property.surface,
            property.numberRoom,
            property.numberBed,
            property.numberBathroom,
            property.description,
            property.photos,
            property.descriptionPhoto,
            property.address,
            property.addressArea,
            property.options,
            property.status,
            property.entryDate,
            property.soldDate,
            property.agentName,
            property.latitude,
            property.longitude
        )
            .test()
            .assertComplete()

        verify(propertyDaoMock).updateProperty(property)
    }*/

    @Test
    fun getOptionsWithPositionInRV_WithSuccess() {
        val optionsBooleanArray : BooleanArray = booleanArrayOf(true, false, true, true, false)
        val expectedArrayValue : MutableList<Option> = mutableListOf(Option.ELEVATOR, Option.PARKING, Option.CLOSE_TO_SHOPS)

        val testValue = viewModel.getOptionsWithPositionInRV(optionsBooleanArray)

        Assert.assertEquals(testValue, expectedArrayValue)
    }

    @Test
    fun getBooleanArrayWithListOptions_WithSuccess() {
        val arrayValue : MutableList<Option> = mutableListOf(Option.ELEVATOR, Option.PARKING, Option.CLOSE_TO_SHOPS)
        val expectedBooleanArray : BooleanArray = booleanArrayOf(true, false, true, true, false)

        val testValue = viewModel.getBooleanArrayWithListOptions(arrayValue)

        Assert.assertArrayEquals(testValue, expectedBooleanArray)
    }
}