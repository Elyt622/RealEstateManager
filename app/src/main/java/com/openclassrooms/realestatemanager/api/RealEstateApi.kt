package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable

interface RealEstateApi {
    fun getAllProperties(): Observable<List<Property>>
}