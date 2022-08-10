package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable

class PropertyViewModel(
    private val propertyDao: PropertyDao
) : ViewModel() {

    fun getPropertyWithRef(ref: Int): Observable<Property>{
        return propertyDao.loadPropertyWithRef(ref)
    }

}