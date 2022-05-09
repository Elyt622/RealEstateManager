package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.di.DI
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable

class PropertyViewModel : ViewModel() {

    private val propertyDao = App.database.propertyDao()

    fun getPropertyWithRef(ref: Int): Observable<Property>{
        return propertyDao.getPropertyWithRef(ref)
    }

}