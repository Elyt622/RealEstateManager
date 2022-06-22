package com.openclassrooms.realestatemanager.viewmodel

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import com.openclassrooms.realestatemanager.utils.Utils
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
        return propertyDao.loadAllProperty()
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
    ) : Single<List<Property>> {
        var listPropertiesWithFilter : List<Property> = listOf()
        return getPropertiesWithTypeFilter(properties, types)
            .flatMap { property1 ->
                getPropertiesWithPriceFilter(
                    property1,
                    minPrice,
                    maxPrice
                )
                    .subscribe { property2 ->
                    listPropertiesWithFilter = Utils.findCommon(property1, property2)
                    getPropertiesWithSurfaceFilter(
                        property2,
                        minSurface,
                        maxSurface
                    )
                        .subscribe { property3 ->
                            listPropertiesWithFilter = Utils.findCommon(listPropertiesWithFilter, property3)
                            getPropertiesWithBedsFilter(
                                property3,
                                minBeds,
                                maxBeds
                            )
                                .subscribe { property4 ->
                                    listPropertiesWithFilter = Utils.findCommon(listPropertiesWithFilter, property4)
                                    getPropertiesWithBathroomsFilter(
                                        property4,
                                        minBathrooms,
                                        maxBathrooms
                                    )
                                        .subscribe { property5 ->
                                            listPropertiesWithFilter = Utils.findCommon(listPropertiesWithFilter, property5)
                                            getPropertiesWithOptionsFilter(
                                                property5,
                                                options
                                            )
                                                .subscribe { property6 ->
                                                    listPropertiesWithFilter = Utils.findCommon(listPropertiesWithFilter, property6)
                                                    getPropertiesWithStatusFilter(
                                                        property6,
                                                        statusList
                                                    )
                                                        .subscribe { property7 ->
                                                            listPropertiesWithFilter = Utils.findCommon(listPropertiesWithFilter, property7)
                                                            getPropertiesWithEntryDateFilter(
                                                                property7,
                                                                startEntryDate,
                                                                endEntryDate
                                                            )
                                                                .subscribe { property8 ->
                                                                    listPropertiesWithFilter = Utils.findCommon(listPropertiesWithFilter, property8)
                                                                    getPropertiesWithSoldDateFilter(
                                                                        property8,
                                                                        startSoldDate,
                                                                        endSoldDate
                                                                    )
                                                                        .subscribe { property9 ->
                                                                            listPropertiesWithFilter = Utils.findCommon(listPropertiesWithFilter, property9)
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                    }
                return@flatMap Single.just(listPropertiesWithFilter)
            }
    }

    private fun getPropertiesWithPriceFilter(properties: List<Property>, minPrice: Int?, maxPrice: Int?) : Single<List<Property>> {
        return if (minPrice != null && maxPrice != null) {
            propertyDao.loadPropertiesWithPriceFilter(minPrice, maxPrice)
        } else if (minPrice == null && maxPrice != null) {
            propertyDao.loadPropertiesWithMaxPriceFilter(maxPrice)
        } else if (minPrice != null && maxPrice == null) {
            propertyDao.loadPropertiesWithMinPriceFilter(minPrice)
        } else {
            return Single.just(properties)
        }
    }

    private fun getPropertiesWithTypeFilter(properties: List<Property>, types: MutableList<Type>) : Single<List<Property>> {
        return if (types.size != 0){
            propertyDao.loadPropertiesWithTypesFilter(types)
        } else {
            Single.just(properties)
        }
    }

    private fun getPropertiesWithSurfaceFilter(properties: List<Property>, minSurface: Float?, maxSurface: Float?) : Single<List<Property>> {
        if (minSurface != null && maxSurface != null) {
            return propertyDao.loadPropertiesWithSurfaceFilter(minSurface, maxSurface)
        } else if (minSurface == null && maxSurface != null) {
            return propertyDao.loadPropertiesWithMaxSurfaceFilter(maxSurface)
        } else if (minSurface != null && maxSurface == null) {
            return propertyDao.loadPropertiesWithMinSurfaceFilter(minSurface)
        }
        return Single.just(properties)
    }

    private fun getPropertiesWithBedsFilter(properties: List<Property>, minBeds: Int?, maxBeds: Int?) : Single<List<Property>> {
        return if (minBeds != null && maxBeds != null) {
            propertyDao.loadPropertiesWithBedroomFilter(minBeds, maxBeds)
        }else if (minBeds == null && maxBeds != null) {
            propertyDao.loadPropertiesWithMaxBedroomFilter(maxBeds)
        } else if (minBeds != null && maxBeds == null) {
            propertyDao.loadPropertiesWithMinBedroomFilter(minBeds)
        } else {
            return Single.just(properties)
        }
    }

    private fun getPropertiesWithBathroomsFilter(properties: List<Property>, minBathrooms: Int?, maxBathrooms: Int?) : Single<List<Property>> {
        return if (minBathrooms != null && maxBathrooms != null) {
            propertyDao.loadPropertiesWithBathroomFilter(minBathrooms, maxBathrooms)
        }else if (minBathrooms == null && maxBathrooms != null) {
            propertyDao.loadPropertiesWithMaxBathroomFilter(maxBathrooms)
        } else if (minBathrooms != null && maxBathrooms == null) {
            propertyDao.loadPropertiesWithMinBathroomFilter(minBathrooms)
        } else {
            return Single.just(properties)
        }
    }

    private fun getPropertiesWithOptionsFilter(properties: List<Property>, options: MutableList<Option>) : Single<List<Property>> {
        return if (options.size != 0){
            propertyDao.loadPropertiesWithOptionsFilter(options)
        } else {
            return Single.just(properties)
        }
    }

    private fun getPropertiesWithStatusFilter(properties: List<Property>, statusList: MutableList<Status>) : Single<List<Property>> {
        return if (statusList.size != 0){
            propertyDao.loadPropertiesWithStatusFilter(statusList)
        } else {
            return Single.just(properties)
        }
    }

    private fun getPropertiesWithEntryDateFilter(properties: List<Property>, minEntryDate: Date?, maxEntryDate: Date?): Single<List<Property>> {
        return if (minEntryDate != null && maxEntryDate != null) {
            propertyDao.loadPropertiesWithEntryDateFilter(minEntryDate, maxEntryDate)
        }else if (minEntryDate == null && maxEntryDate != null) {
            propertyDao.loadPropertiesWithMaxEntryDateFilter(maxEntryDate)
        } else if (minEntryDate != null && maxEntryDate == null) {
            propertyDao.loadPropertiesWithMinEntryDateFilter(minEntryDate)
        } else {
            Single.just(properties)
        }
    }

    private fun getPropertiesWithSoldDateFilter(properties: List<Property>, minSoldDate: Date?, maxSoldDate: Date?): Single<List<Property>> {
        return if (minSoldDate != null && maxSoldDate != null) {
            propertyDao.loadPropertiesWithSoldDateFilter(minSoldDate, maxSoldDate)
        }else if (minSoldDate == null && maxSoldDate != null) {
            propertyDao.loadPropertiesWithMaxSoldDateFilter(maxSoldDate)
        } else if (minSoldDate != null && maxSoldDate == null) {
            propertyDao.loadPropertiesWithMinSoldDateFilter(minSoldDate)
        } else {
            Single.just(properties)
        }
    }

    fun getPropertiesWithAscPriceSort(): Observable<List<Property>> {
        return propertyDao.loadAllPropertiesWithAscPriceSort()
    }

    fun getPropertiesWithDescPriceSort() : Observable<List<Property>> {
        return propertyDao.loadAllPropertiesWithDescPriceSort()
    }

    fun getPropertiesWithTypeSort() : Observable<List<Property>> {
        return propertyDao.loadAllPropertiesWithTypeSort()
    }

    fun getPropertiesWithStatusSort() : Observable<List<Property>> {
        return propertyDao.loadAllPropertiesWithStatusSort()
    }
}