package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Property

class FakeRealEstateApi : RealEstateApi{

    override fun getAllProperties(): List<Property> {
        return ModelGenerator().getAllProperties()
    }
}