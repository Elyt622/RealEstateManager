package com.openclassrooms.realestatemanager.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.Type
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class AddPropertyViewModel(
    private val propertyDao: PropertyDao
) : ViewModel() {

    var type: Type? = null

    var options: MutableList<Option>? = null

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
        type: Type?,
        surface: Float?,
        price: Int?,
        numberRoom: Int?,
        numberBed: Int?,
        numberBathroom: Int?,
        description: String,
        address: String,
        addressArea: String,
        photos: MutableList<Uri>,
        descriptionPhoto: MutableList<String>,
        latitude: Double?,
        longitude: Double?,
        entryDate: Date,
        options: MutableList<Option>?,
        agentName: String
    ): Completable =
        Observable.just(Property())
            .map {
                if (photos.size == 0) throw Exception("PHOTO_IS_EMPTY")
                if (type == null) throw Exception("NO_SELECTED_TYPE")
                if (numberRoom == null) throw Exception("ROOM_IS_EMPTY")
                if (numberBed == null) throw Exception("BED_IS_EMPTY")
                if (numberBathroom == null) throw Exception("BATHROOM_IS_EMPTY")
                if (price == null) throw Exception("PRICE_IS_EMPTY")
                if (address.isEmpty()) throw Exception("ADDRESS_IS_EMPTY")
                if (description.isEmpty()) throw Exception("DESC_IS_EMPTY")
                if (longitude == null || latitude == null) throw Exception("LOCATION_IS_INVALID")
                if (agentName.isEmpty()) throw Exception("AGENT_IS_EMPTY")
                it.apply {
                    this.type = type
                    this.surface = surface
                    this.price = price
                    this.numberRoom = numberRoom
                    this.numberBathroom = numberBathroom
                    this.numberBed = numberBed
                    this.description = description
                    this.address = address
                    this.addressArea = addressArea
                    this.photos = photos
                    this.latitude = latitude
                    this.longitude = longitude
                    this.entryDate = entryDate
                    this.options = options
                    this.agentName = agentName
                    this.descriptionPhoto = descriptionPhoto
                }
            }
            .flatMapCompletable {
                propertyDao.insertProperty(it).subscribeOn(Schedulers.io())
            }
}