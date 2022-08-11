package com.openclassrooms.realestatemanager.viewmodel

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import io.reactivex.rxjava3.core.Completable
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class ModifyPropertyViewModelTests {

    private lateinit var viewModel: ModifyPropertyViewModel

    private val propertyDaoMock: PropertyDao = mock()

    private val property = Property(2,
        Type.APARTMENT,
        100000,
        100.0F,
        8,
        4,
        3,
        "Description",
        mutableListOf(mock()),
        mutableListOf("Description Photo"),
        "Address",
        "MANHATTAN",
        mutableListOf(Option.PARKING),
        Status.ON_SALE,
        Date(),
        Date(),
        "Agent",
        78.0,
        -30.0
    )

    @Before
    fun setup() {
        viewModel = ModifyPropertyViewModel(propertyDaoMock)
    }

    @After
    fun teardown() {
        //verifyNoMoreInteractions(propertyDaoMock)
    }

    @Test
    fun updatePropertyTest(){

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
            .awaitDone(5, TimeUnit.SECONDS)
            .assertComplete()
    }

    @Test
    fun getOptionsWithPositionInRVTest() {
        val optionsBooleanArray : BooleanArray = booleanArrayOf(true, false, true, true, false)
        val expectedArrayValue : MutableList<Option> = mutableListOf(Option.ELEVATOR, Option.PARKING, Option.CLOSE_TO_SHOPS)

        val testValue = viewModel.getOptionsWithPositionInRV(optionsBooleanArray)

        Assert.assertEquals(testValue, expectedArrayValue)
    }

    @Test
    fun getBooleanArrayWithListOptionsTest() {
        val arrayValue : MutableList<Option> = mutableListOf(Option.ELEVATOR, Option.PARKING, Option.CLOSE_TO_SHOPS)
        val expectedBooleanArray : BooleanArray = booleanArrayOf(true, false, true, true, false)

        val testValue = viewModel.getBooleanArrayWithListOptions(arrayValue)

        Assert.assertArrayEquals(testValue, expectedBooleanArray)
    }
}