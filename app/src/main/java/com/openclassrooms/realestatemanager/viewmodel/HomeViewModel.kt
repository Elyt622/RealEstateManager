package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class HomeViewModel(
    private val propertyDao: PropertyDao
) : ViewModel() {

    fun getAllProperties(): Observable<List<Property>>{
        return propertyDao.loadAllProperty()
    }

    fun getPropertiesWithFilter(query: SupportSQLiteQuery): Single<List<Property>> {
        return propertyDao.filter(query)
    }

    fun getPositionAdapterWithRef(ref: Int, properties : List<Property>) : Int{
        for (property in properties){
            if (property.ref == ref)
                return properties.indexOf(property)
            }
        return 0
    }
}