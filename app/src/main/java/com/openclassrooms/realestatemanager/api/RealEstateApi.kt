package com.openclassrooms.realestatemanager.api

import com.openclassrooms.realestatemanager.model.Property

interface RealEstateApi {
    fun getAllProperties(): List<Property>
}