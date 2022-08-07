package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.database.dao.PropertyDao

class ViewModelFactory (val propertyDao: PropertyDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  modelClass.getConstructor(PropertyDao::class.java)
            .newInstance(propertyDao)
    }

}