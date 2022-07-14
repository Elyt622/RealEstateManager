package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable

class HomeViewModel : ViewModel() {

    private val propertyDao = App.database.propertyDao()

    fun getAllProperties(): Observable<List<Property>>{
        return propertyDao.loadAllProperty()
    }
}