package com.openclassrooms.realestatemanager.viewmodel

import androidx.sqlite.db.SupportSQLiteQuery
import com.nhaarman.mockitokotlin2.*
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.After
import org.junit.Before
import org.junit.Test

class HomeViewModelTests {

    private lateinit var viewModel: HomeViewModel

    private val propertyDaoMock: PropertyDao = mock()

    @Before
    fun setup() {
        viewModel = HomeViewModel(propertyDaoMock)
    }

    @After
    fun teardown() {
        verifyNoMoreInteractions(propertyDaoMock)
    }

    @Test
    fun getAllPropertiesTest() {
        val expectedResult : List<Property> = arrayListOf()

        whenever(propertyDaoMock.loadAllProperty())
            .thenReturn(Observable.just(expectedResult))

        viewModel.getAllProperties()
            .test()
            .assertValue(expectedResult)

        verify(propertyDaoMock).loadAllProperty()
    }

    @Test
    fun getPropertiesWithFilterTest() {
        val expectedResult: List<Property> = arrayListOf()
        val sqLiteQuery: SupportSQLiteQuery = mock()

        whenever(propertyDaoMock.filter(any()))
            .thenReturn(Single.just(expectedResult))

        viewModel.getPropertiesWithFilter(sqLiteQuery)
            .test()
            .assertValue(expectedResult)

        verify(propertyDaoMock).filter(sqLiteQuery)
    }
}