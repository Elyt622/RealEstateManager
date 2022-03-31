package com.openclassrooms.realestatemanager.di

import com.openclassrooms.realestatemanager.api.FakeRealEstateApi
import com.openclassrooms.realestatemanager.api.RealEstateApi

class DI {
    private var service : RealEstateApi = FakeRealEstateApi()

    fun getService(): RealEstateApi {
        return service
    }
}