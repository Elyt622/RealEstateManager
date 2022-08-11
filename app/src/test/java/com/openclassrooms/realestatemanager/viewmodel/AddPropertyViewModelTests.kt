package com.openclassrooms.realestatemanager.viewmodel

import com.nhaarman.mockitokotlin2.*
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Type
import io.reactivex.rxjava3.core.Completable
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class AddPropertyViewModelTests {

    private var propertyDao: PropertyDao = mock()

    private lateinit var viewModel : AddPropertyViewModel

    @Before
    fun setup(){
        viewModel = AddPropertyViewModel(propertyDao)
    }

    @After
    fun teardown() {
        verifyNoMoreInteractions(propertyDao)
    }

    @Test
    fun insertPropertyTest(){

        whenever(propertyDao.insertProperty(any()))
            .thenReturn(Completable.complete())

        viewModel.insertProperty(
            Type.APARTMENT,
            100.0F,
            100000,
            8,
            4,
            3,
            "Description",
            "Times Square",
            "MANHATTAN",
            mutableListOf(mock()),
            mutableListOf("Description Photo"),
            78.0,
            -30.0,
            Date(),
            mutableListOf(Option.PARKING),
            "Agent",
        )
            .test()
            .awaitDone(5, TimeUnit.SECONDS)
            .assertComplete()

        verify(propertyDao).insertProperty(any())
    }
}