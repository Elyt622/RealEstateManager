package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import io.reactivex.rxjava3.core.Observable
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

    fun setStartEntryDate(startEntryDate: Date) {
        this.startEntryDate = startEntryDate
    }

    fun setEndEntryDate(endEntryDate: Date) {
        this.endEntryDate = endEntryDate
    }

    fun setStartSoldDate(startSoldDate: Date) {
        this.startSoldDate = startSoldDate
    }

    fun setEndSoldDate(endSoldDate: Date) {
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
        return propertyDao.getAllProperty()
    }

    fun applyAllFilters(
        properties: List<Property>,
        types: MutableList<Type>,
        minPrice: Int?,
        maxPrice: Int?,
        minSurface: Float?,
        maxSurface: Float?,
        minBeds: Int?,
        maxBeds: Int?,
        minBathrooms: Int?,
        maxBathrooms: Int?,
        options: MutableList<Option>,
        statusList: MutableList<Status>,
        startEntryDate: Date?,
        endEntryDate: Date?,
        startSoldDate: Date?,
        endSoldDate: Date?
    ) : List<Property> {
        var propertiesWithAllFilters: List<Property> = getPropertiesWithTypeFilter(properties, types)
        propertiesWithAllFilters = getPropertiesWithPriceFilter(propertiesWithAllFilters, minPrice, maxPrice)
        propertiesWithAllFilters = getPropertiesWithSurfaceFilter(propertiesWithAllFilters, minSurface, maxSurface)
        propertiesWithAllFilters = getPropertiesWithBedsFilter(propertiesWithAllFilters, minBeds, maxBeds)
        propertiesWithAllFilters = getPropertiesWithBathroomsFilter(propertiesWithAllFilters, minBathrooms, maxBathrooms)
        propertiesWithAllFilters = getPropertiesWithOptionsFilter(propertiesWithAllFilters, options)
        propertiesWithAllFilters = getPropertiesWithStatusFilter(propertiesWithAllFilters, statusList)
        propertiesWithAllFilters = getPropertiesWithEntryDateFilter(propertiesWithAllFilters, startEntryDate, endEntryDate)
        propertiesWithAllFilters = getPropertiesWithSoldDateFilter(propertiesWithAllFilters, startSoldDate, endSoldDate)
        return propertiesWithAllFilters
    }

    private fun getPropertiesWithPriceFilter(properties: List<Property>, minPrice: Int?, maxPrice: Int?) : List<Property> {
        val propertiesWithFilter: MutableList<Property> = mutableListOf()
        if (minPrice != null && maxPrice != null) {
            for (property in properties) {
                if (property.price >= minPrice && property.price <= maxPrice)
                    propertiesWithFilter.add(property)
            }
        }else if (minPrice == null && maxPrice != null) {
            for (property in properties) {
                if (property.price <= maxPrice)
                    propertiesWithFilter.add(property)
            }
        } else if (minPrice != null && maxPrice == null) {
            for (property in properties) {
                if (property.price >= minPrice)
                    propertiesWithFilter.add(property)
            }
        } else {
            return properties
        }
        return propertiesWithFilter
    }

    private fun getPropertiesWithTypeFilter(properties: List<Property>, types: MutableList<Type>) : List<Property> {
        val propertiesWithFilter: MutableList<Property> = mutableListOf()
        if (types.size != 0){
            for (type in types){
                for (property in properties){
                    if (property.type == type)
                        propertiesWithFilter.add(property)
                }
            }
        } else {
            return properties
        }
        return propertiesWithFilter
    }

    private fun getPropertiesWithSurfaceFilter(properties: List<Property>, minSurface: Float?, maxSurface: Float?) : List<Property> {
        val propertiesWithFilter: MutableList<Property> = mutableListOf()
        if (minSurface != null && maxSurface != null) {
            for (property in properties) {
                if (property.surface != null) {
                    if (property.surface!! in minSurface..maxSurface)
                        propertiesWithFilter.add(property)
                }
            }
        }else if (minSurface == null && maxSurface != null) {
            for (property in properties) {
                if (property.surface != null) {
                    if (property.surface!! <= maxSurface)
                        propertiesWithFilter.add(property)
                }
            }
        } else if (minSurface != null && maxSurface == null) {
            for (property in properties) {
                if (property.surface != null) {
                    if (property.surface!! >= minSurface)
                        propertiesWithFilter.add(property)
                }
            }
        } else {
            return properties
        }
        return propertiesWithFilter
    }

    private fun getPropertiesWithBedsFilter(properties: List<Property>, minBeds: Int?, maxBeds: Int?) : List<Property> {
        val propertiesWithFilter: MutableList<Property> = mutableListOf()
        if (minBeds != null && maxBeds != null) {
            for (property in properties) {
                if (property.numberBed >= minBeds && property.numberBed <= maxBeds)
                    propertiesWithFilter.add(property)
            }
        }else if (minBeds == null && maxBeds != null) {
            for (property in properties) {
                if (property.numberBed <= maxBeds)
                    propertiesWithFilter.add(property)
            }
        } else if (minBeds != null && maxBeds == null) {
            for (property in properties) {
                if (property.numberBed >= minBeds)
                    propertiesWithFilter.add(property)
            }
        } else {
            return properties
        }
        return propertiesWithFilter
    }

    private fun getPropertiesWithBathroomsFilter(properties: List<Property>, minBathrooms: Int?, maxBathrooms: Int?) : List<Property> {
        val propertiesWithFilter: MutableList<Property> = mutableListOf()
        if (minBathrooms != null && maxBathrooms != null) {
            for (property in properties) {
                if (property.numberBathroom >= minBathrooms && property.numberBathroom <= maxBathrooms)
                    propertiesWithFilter.add(property)
            }
        }else if (minBathrooms == null && maxBathrooms != null) {
            for (property in properties) {
                if (property.numberBathroom <= maxBathrooms)
                    propertiesWithFilter.add(property)
            }
        } else if (minBathrooms != null && maxBathrooms == null) {
            for (property in properties) {
                if (property.numberBathroom >= minBathrooms)
                    propertiesWithFilter.add(property)
            }
        } else {
            return properties
        }
        return propertiesWithFilter
    }

    private fun getPropertiesWithOptionsFilter(properties: List<Property>, options: MutableList<Option>) : List<Property> {
        val propertiesWithFilter: MutableList<Property> = mutableListOf()
        if (options.size != 0){
            for (option in options) {
                for (property in properties) {
                    if (property.options?.contains(option) == true)
                        propertiesWithFilter.add(property)
                }
            }
        } else {
            return properties
        }
        return propertiesWithFilter
    }

    private fun getPropertiesWithStatusFilter(properties: List<Property>, statusList: MutableList<Status>) : List<Property> {
        val propertiesWithFilter: MutableList<Property> = mutableListOf()
        if (status.size != 0){
            for (status in statusList) {
                for (property in properties) {
                    if (property.status == status)
                        propertiesWithFilter.add(property)
                }
            }
        } else {
            return properties
        }
        return propertiesWithFilter
    }

    private fun getPropertiesWithEntryDateFilter(properties: List<Property>, minEntryDate: Date?, maxEntryDate: Date?): List<Property> {
        val propertiesWithFilter: MutableList<Property> = mutableListOf()
        if (minEntryDate != null && maxEntryDate != null) {
            for (property in properties) {
                if (property.entryDate.after(minEntryDate) && property.entryDate.before(maxEntryDate))
                    propertiesWithFilter.add(property)
            }
        }else if (minEntryDate == null && maxEntryDate != null) {
            for (property in properties) {
                if (property.entryDate.before(maxEntryDate))
                    propertiesWithFilter.add(property)
            }
        } else if (minEntryDate != null && maxEntryDate == null) {
            for (property in properties) {
                if (property.entryDate.after(minEntryDate))
                    propertiesWithFilter.add(property)
            }
        } else {
            return properties
        }
        return propertiesWithFilter
    }

    private fun getPropertiesWithSoldDateFilter(properties: List<Property>, minSoldDate: Date?, maxSoldDate: Date?): List<Property> {
        val propertiesWithFilter: MutableList<Property> = mutableListOf()
        if (minSoldDate != null && maxSoldDate != null) {
            for (property in properties) {
                if (property.soldDate != null)
                    if (property.soldDate?.after(minSoldDate)!! && property.soldDate?.before(maxSoldDate)!!)
                        propertiesWithFilter.add(property)
            }
        }else if (minSoldDate == null && maxSoldDate != null) {
            for (property in properties) {
                if (property.soldDate != null)
                    if (property.soldDate?.before(maxSoldDate)!!)
                    propertiesWithFilter.add(property)
            }
        } else if (minSoldDate != null && maxSoldDate == null) {
            for (property in properties) {
                if (property.soldDate != null)
                    if (property.soldDate?.after(minSoldDate)!!)
                        propertiesWithFilter.add(property)
            }
        } else {
            return properties
        }
        return propertiesWithFilter
    }

    fun getPropertiesWithAscPriceSort(): Observable<List<Property>> {
        return propertyDao.getAllPropertiesWithAscPriceSort()
    }

    fun getPropertiesWithDescPriceSort() : Observable<List<Property>> {
        return propertyDao.getAllPropertiesWithDescPriceSort()
    }

    fun getPropertiesWithTypeSort() : Observable<List<Property>> {
        return propertyDao.getAllPropertiesWithTypeSort()
    }

    fun getPropertiesWithStatusSort() : Observable<List<Property>> {
        return propertyDao.getAllPropertiesWithStatusSort()
    }
}