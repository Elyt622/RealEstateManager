package com.openclassrooms.realestatemanager.viewmodel

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable
import org.junit.After
import org.junit.Before
import org.junit.Test

class PropertyViewModelTests {

    private lateinit var viewModel: PropertyViewModel

    private val propertyDaoMock: PropertyDao = mock()

    private val property : Property = mock()

    @Before
    fun setup() {
        viewModel = PropertyViewModel(propertyDaoMock)
    }

    @After
    fun teardown() {
        verifyNoMoreInteractions(propertyDaoMock)
    }

    @Test
    fun getPropertyWithRefTest() {
        val expectedResult = property

        whenever(propertyDaoMock.loadPropertyWithRef(property.ref))
            .thenReturn(Observable.just(property))

        viewModel.getPropertyWithRef(property.ref)
            .test()
            .assertValue(expectedResult)

        verify(propertyDaoMock).loadPropertyWithRef(property.ref)
    }
}