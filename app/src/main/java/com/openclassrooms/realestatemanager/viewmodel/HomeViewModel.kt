package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class HomeViewModel : ViewModel() {

    private val propertyDao = App.database.propertyDao()

    fun getAllProperties(): Observable<List<Property>>{
        return propertyDao.loadAllProperty()
    }

    fun getPropertiesWithFilter(query: SupportSQLiteQuery): Single<List<Property>> {
        return propertyDao.filter(query)
    }
}