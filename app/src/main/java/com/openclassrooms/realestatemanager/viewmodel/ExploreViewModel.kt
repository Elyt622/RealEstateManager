package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.*

class ExploreViewModel : ViewModel() {

    private val propertyDao = App.database.propertyDao()

    private var types: MutableList<Type> = mutableListOf()

    private var options: MutableList<Option> = mutableListOf()

    private var status: MutableList<Status> = mutableListOf()

    private var startEntryDate: Date? = null

    private var endEntryDate: Date? = null

    private var startSoldDate: Date? = null

    private var endSoldDate: Date? = null

    fun getStartEntryDate() = this.startEntryDate

    fun getEndEntryDate() = this.endEntryDate

    fun getStartSoldDate() = this.startSoldDate

    fun getEndSoldDate() = this.endSoldDate

    fun setStartEntryDate(startEntryDate: Date?) {
        this.startEntryDate = startEntryDate
    }

    fun setEndEntryDate(endEntryDate: Date?) {
        this.endEntryDate = endEntryDate
    }

    fun setStartSoldDate(startSoldDate: Date?) {
        this.startSoldDate = startSoldDate
    }

    fun setEndSoldDate(endSoldDate: Date?) {
        this.endSoldDate = endSoldDate
    }

    fun getTypes() = this.types

    fun setTypes(types: MutableList<Type>) {
        this.types = types
    }

    fun getOptions() = this.options

    fun setOptions(options: MutableList<Option>) {
        this.options = options
    }

    fun getStatus() = this.status

    fun setStatus(status: MutableList<Status>) {
        this.status = status
    }

    fun getTypeWithPositionInRV(elements: BooleanArray): MutableList<Type> {
        val types: MutableList<Type> = mutableListOf()
        for (type in Type.values()) {
            if (elements[type.ordinal]) {
                types.add(type)
            }
        }
        return types
    }

    fun getOptionsWithPositionInRV(elements: BooleanArray): MutableList<Option> {
        val options: MutableList<Option> = mutableListOf()
        for (option in Option.values()) {
            if (elements[option.ordinal]) {
                options.add(option)
            }
        }
        return options
    }

    fun getStatusWithPositionInRV(elements: BooleanArray): MutableList<Status> {
        val statusList: MutableList<Status> = mutableListOf()
        for (status in Status.values()) {
            if (elements[status.ordinal]) {
                statusList.add(status)
            }
        }
        return statusList
    }

    fun getAllProperties(): Observable<List<Property>> {
        return propertyDao.loadAllProperty()
    }

    fun loadAllAddressArea() : Single<List<String>> {
        return propertyDao.loadAllAddressArea()
    }

    private fun <T> getQueryForSelect(list: List<T>): String{
        val stringBuilder = StringBuilder()
        stringBuilder.append("(")
        for (element in list) {
            if (list.size > 1) {
                if (list.last() != element)
                    stringBuilder.append("\"$element\",")
                else
                    stringBuilder.append("\"$element\"")
            } else {
                stringBuilder.append("\"$element\"")
            }
        }
        stringBuilder.append(")")
        return stringBuilder.toString()
    }

    private fun <T> getQueryForSelectOptions(list: List<T>): String{
        val stringBuilder = StringBuilder()
        for (element in list) {
            if (list.size > 1) {
                if (list.last() != element)
                    stringBuilder.append("\"%$element%\" OR options LIKE ")
                else
                    stringBuilder.append("\"%$element%\"")
            } else {
                stringBuilder.append("\"%$element%\"")
            }
        }
        return stringBuilder.toString()
    }

    fun setMarkerOnMap(map : SupportMapFragment, properties: List<Property>) {
        map.getMapAsync{
            it.clear()
            for (property in properties) {
                val marker = it.addMarker(
                    MarkerOptions()
                        .position(LatLng(property.latitude, property.longitude))
                        .title(property.address)
                        .snippet(property.type.name)
                )
                marker?.tag = property.ref
            }
        }
    }

    fun applyAllFilters(
        addressArea: String,
        types: List<Type>,
        minPrice: Int?,
        maxPrice: Int?,
        minSurface: Float?,
        maxSurface: Float?,
        minBeds: Int?,
        maxBeds: Int?,
        minBathrooms: Int?,
        maxBathrooms: Int?,
        options: List<Option>,
        statusList: List<Status>,
        startEntryDate: Date?,
        endEntryDate: Date?,
        startSoldDate: Date?,
        endSoldDate: Date?
    ) : Single<List<Property>> {
        var stringBuilder = StringBuilder()

        if(addressArea.isNotEmpty()) stringBuilder.append("addressArea IS \"$addressArea\" AND ")
        if(types.isNotEmpty()) stringBuilder.append("type IN ${getQueryForSelect(types)} AND ")
        if(minPrice != null) stringBuilder.append("price >= $minPrice AND ")
        if(maxPrice != null) stringBuilder.append("price <= $maxPrice AND ")
        if(minSurface != null) stringBuilder.append("surface >= $minSurface AND ")
        if(maxSurface != null) stringBuilder.append("surface <= $maxSurface AND ")
        if(minBeds != null) stringBuilder.append("numberBed >= $minBeds AND ")
        if(maxBeds != null) stringBuilder.append("numberBed <= $maxBeds AND ")
        if(minBathrooms != null) stringBuilder.append("numberBathroom >= $minBathrooms AND ")
        if(maxBathrooms != null) stringBuilder.append("numberBathroom <= $maxBathrooms AND ")
        if(options.isNotEmpty()) stringBuilder.append("(options LIKE ${getQueryForSelectOptions(options)}) AND ")
        if(statusList.isNotEmpty()) stringBuilder.append("status IN ${getQueryForSelect(statusList)} AND ")
        if(startEntryDate != null) stringBuilder.append("entryDate >= ${startEntryDate.time} AND ")
        if(endEntryDate != null) stringBuilder.append("entryDate <= ${endEntryDate.time} AND ")
        if(startSoldDate != null) stringBuilder.append("soldDate >= ${startSoldDate.time} AND ")
        if(endSoldDate != null) stringBuilder.append("soldDate <= ${endSoldDate.time} AND")

        if(stringBuilder.isNotEmpty()){
            val lastIdx = stringBuilder.lastIndexOf(" AND")
            stringBuilder = StringBuilder(stringBuilder).replace(lastIdx, lastIdx+4, ";")
        }

        val query = SimpleSQLiteQuery(
            "SELECT * FROM Property WHERE $stringBuilder"
        )

        return propertyDao.filter(query)
    }
}