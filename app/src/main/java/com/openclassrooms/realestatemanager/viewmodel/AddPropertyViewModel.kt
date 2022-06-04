package com.openclassrooms.realestatemanager.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.Type
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class AddPropertyViewModel : ViewModel() {

    private val propertyDao = App.database.propertyDao()

    private lateinit var type: Type

    private var options: MutableList<Option>? = null

    fun getType() = type

    fun setType(type: Type){
        this.type = type
    }

    fun getOptions() = options

    fun setOptions(options: MutableList<Option>?){
        this.options = options
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
        photos: MutableList<Uri>,
        latitude: Double,
        longitude: Double,
        entryDate: Date
    ): Completable =
        Observable.just(Property())
            .map {
                if (address.isEmpty()){
                    throw Exception("ADDRESS_IS_EMPTY")
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
                    this.latitude = latitude
                    this.longitude = longitude
                    this.entryDate = entryDate
                }
            }
            .flatMapCompletable {
                propertyDao.insertProperty(it).subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
}