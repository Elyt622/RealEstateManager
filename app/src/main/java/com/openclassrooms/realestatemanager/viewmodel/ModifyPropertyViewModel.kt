package com.openclassrooms.realestatemanager.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class ModifyPropertyViewModel : ViewModel() {

    private val propertyDao = App.database.propertyDao()

    private lateinit var type: Type

    private var soldDate: Date? = null

    private var options: MutableList<Option>? = null

    fun getType() = type

    fun setType(type: Type){
        this.type = type
    }

    fun getOptions() = options

    fun setOptions(options: MutableList<Option>?){
        this.options = options
    }

    fun getSoldDate() = soldDate

    fun setSoldDate(soldDate: Date?) {
        this.soldDate = soldDate
    }

    fun getPropertyWithRef(ref: Int): Observable<Property> {
        return propertyDao.loadPropertyWithRef(ref)
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

    fun updateProperty(
        ref: Int,
        type: Type,
        price: Int?,
        surface: Float?,
        numberRoom: Int?,
        numberBed: Int?,
        numberBathroom: Int?,
        description: String,
        photos: MutableList<Uri>,
        address: String,
        options: MutableList<Option>?,
        status: Status,
        entryDate: Date,
        soldDate: Date?,
        agentName: String,
        latitude: Double?,
        longitude: Double?
    ): Completable =
        Observable.just(Property())
            .map {
                if (photos.size == 0) throw Exception("PHOTO_IS_EMPTY")
                if (numberRoom == null) throw Exception("ROOM_IS_EMPTY")
                if (numberBed == null) throw Exception("BED_IS_EMPTY")
                if (numberBathroom == null) throw Exception("BATHROOM_IS_EMPTY")
                if (price == null) throw Exception("PRICE_IS_EMPTY")
                if (address.isEmpty()) throw Exception("ADDRESS_IS_EMPTY")
                if (description.isEmpty()) throw Exception("DESC_IS_EMPTY")
                if (longitude == null || latitude == null) throw Exception("LOCATION_IS_INVALID")
                if (agentName.isEmpty()) throw Exception("AGENT_IS_EMPTY")
                if (status == Status.SOLD && soldDate == null) throw Exception("SOLD_DATE_IS_EMPTY")
                it.apply {
                    this.ref = ref
                    this.type = type
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
                    this.soldDate = soldDate
                    this.options = options
                    this.agentName = agentName
                    this.status = status
                }
            }
            .flatMapCompletable {
                propertyDao.updatePropertyWithRef(it).subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())

}