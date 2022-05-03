package com.openclassrooms.realestatemanager.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class AddPropertyViewModel : ViewModel() {

    private var newProperty : Observable<Property> = Observable.just(Property())

    private val propertyDao = App.database.propertyDao()

    fun getNewProperty() : Observable<Property>{
        return newProperty
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

    fun insertPropertyInDatabase(property: Property) {
        propertyDao.insertProperty(property).subscribeOn(Schedulers.io()).subscribeBy(
            onSuccess = {
                        Log.d("ADDPROPERTY", "INSERT WITH SUCCESS")
            },
            onError = {
                Log.d("ADDPROPERTY", "INSERT FAILED: " + it.message.toString())
            })
        }

}