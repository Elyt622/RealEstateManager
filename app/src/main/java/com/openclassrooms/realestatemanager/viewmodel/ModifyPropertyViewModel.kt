package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable

class ModifyPropertyViewModel : ViewModel() {

    private var newProperty : Observable<Property> = Observable.just(Property())

    private val propertyDao = App.database.propertyDao()

    fun getNewProperty() : Observable<Property>{
        return newProperty
    }

    fun getPropertyWithRef(ref: Int): Observable<Property> {
        return propertyDao.getPropertyWithRef(ref)
    }

    fun getOptionsWithPositionInRV(elements: BooleanArray) : MutableList<Option> {
        val options : MutableList<Option> = mutableListOf()
        for (option in Option.values()) {
            if(elements[option.ordinal]){
                options.add(option)
            }
        }
        return options
    }

    fun getBooleanArrayWithListOptions(listOptions: MutableList<Option>?): BooleanArray {
        val booleanArray = BooleanArray(Option.values().size) {false}
        if (listOptions != null) {
            for (option in listOptions) {
                booleanArray[option.ordinal] = true
            }
        }
        return booleanArray
    }

}