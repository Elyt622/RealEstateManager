package com.openclassrooms.realestatemanager.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

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

    fun insertProperty(
        surface: Float,
        price: Int,
        numberRoom: Int,
        numberBed: Int,
        numberBathroom: Int,
        description: String,
        address: String,
        photos: MutableList<Uri>
    ): Completable =
        newProperty
            .map {
                if (address.isEmpty()){
                    throw Exception("Address is empty")
                }
                it.apply {
                    this.surface = surface
                    this.price = price
                    this.numberRoom = numberRoom
                    this.numberBathroom = numberBathroom
                    this.numberBed = numberBed
                    this.description = description
                    this.address = address
                    this.photos = photos
                }
            }
            .flatMapCompletable {
                propertyDao.insertProperty(it).subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
}