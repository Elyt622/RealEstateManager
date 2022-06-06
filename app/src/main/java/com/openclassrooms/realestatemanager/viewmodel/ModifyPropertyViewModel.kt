package com.openclassrooms.realestatemanager.viewmodel

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

    fun updateProperty(property: Property) : Completable {
        return Completable.fromSingle(propertyDao.updatePropertyWithRef(property)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()))
    }

}