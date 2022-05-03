package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.di.DI
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable

class PropertyViewModel : ViewModel() {

    private val api = DI().getService()

    private val propertyDao = App.database.propertyDao()

    private fun getAllProperties(): Observable<List<Property>> = api.getAllProperties()

    //private fun getAllProperties(): Observable<List<Property>> = propertyDao.getAllProperty()

    fun getPropertyWithRef(ref: Int): Property? {
        var property : Property? = null
        getAllProperties().subscribe { results ->
            for (result in results) {
                if (result.ref == ref)
                     property = result
            }
        }
        return property
    }

}