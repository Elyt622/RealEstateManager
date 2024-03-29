package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import io.reactivex.rxjava3.core.Single
import java.util.*

class ExploreViewModel(
    private val propertyDao: PropertyDao
) : ViewModel() {

    var types: MutableList<Type> = mutableListOf()

    var options: MutableList<Option> = mutableListOf()

    var status: MutableList<Status> = mutableListOf()

    var startEntryDate: Date? = null

    var endEntryDate: Date? = null

    var startSoldDate: Date? = null

    var endSoldDate: Date? = null

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

    fun loadAllAddressArea() : Single<List<String>> {
        return propertyDao.loadAllAddressArea()
    }

    fun <T> getQueryForSelect(list: List<T>): String{
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

    fun <T> getQueryForSelectOptions(list: List<T>): String{
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

    fun applyAllFilters(
        addressArea: String,
        types: List<Type>,
        minPhotos: Int?,
        maxPhotos: Int?,
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
    ) : String {
        val stringQuery : String
        var stringBuilder = StringBuilder()

        if(addressArea.isNotEmpty()) stringBuilder.append("addressArea IS \"$addressArea\" AND ")
        if(types.isNotEmpty()) stringBuilder.append("type IN ${getQueryForSelect(types)} AND ")
        if(minPhotos != null) stringBuilder.append("(SELECT length(photos) - length(replace(photos, ',', '')) + 1 >= $minPhotos) AND ")
        if(maxPhotos != null) stringBuilder.append("(SELECT length(photos) - length(replace(photos, ',', '')) + 1 <= $maxPhotos) AND ")
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
        if(endSoldDate != null) stringBuilder.append("soldDate <= ${endSoldDate.time} AND ")

        if(stringBuilder.isNotEmpty()){
            val lastIdx = stringBuilder.lastIndexOf(" AND ")
            stringBuilder = StringBuilder(stringBuilder).replace(lastIdx, lastIdx+5, ";")
            stringQuery = "SELECT * FROM Property WHERE $stringBuilder"
        } else {
            stringQuery = ""
        }

        return stringQuery
    }
}