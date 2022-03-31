package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.di.DI
import com.openclassrooms.realestatemanager.model.Property

class HomeViewModel : ViewModel() {

    private val api = DI().getService()

    fun getAllProperties(): List<Property> {
        return api.getAllProperties()
    }
}