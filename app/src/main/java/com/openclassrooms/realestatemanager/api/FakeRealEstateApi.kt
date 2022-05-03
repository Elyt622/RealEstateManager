package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable

class FakeRealEstateApi : RealEstateApi{

    override fun getAllProperties(): Observable<List<Property>> {
        return Observable.just(ModelGenerator().getAllProperties())
    }
}