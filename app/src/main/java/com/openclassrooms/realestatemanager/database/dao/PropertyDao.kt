package com.openclassrooms.realestatemanager.database.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.model.Option
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.Status
import com.openclassrooms.realestatemanager.model.Type
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.*

@Dao
interface PropertyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProperty(property: Property) : Completable

    @Query("SELECT * FROM Property")
    fun loadAllProperty() : Observable<List<Property>>

    @Query("SELECT * FROM Property WHERE ref = :ref")
    fun loadPropertyWithRef(ref: Int) : Observable<Property>

    @Update
    fun updatePropertyWithRef(property: Property) : Completable

    // Filter
    // Type
    @Query("SELECT * FROM Property where type IN (:types)")
    fun loadPropertiesWithTypesFilter(types: List<Type>) : Single<List<Property>>

    // Price
    @Query ("SELECT * FROM Property where price BETWEEN :minPrice AND :maxPrice")
    fun loadPropertiesWithPriceFilter(minPrice: Int, maxPrice: Int) : Single<List<Property>>

    @Query ("SELECT * FROM Property where price BETWEEN :minPrice AND (SELECT MAX(price) FROM Property)")
    fun loadPropertiesWithMinPriceFilter(minPrice: Int) : Single<List<Property>>

    @Query ("SELECT * FROM Property where price BETWEEN 0 AND :maxPrice")
    fun loadPropertiesWithMaxPriceFilter(maxPrice: Int) : Single<List<Property>>

    // Surface
    @Query ("SELECT * FROM Property where surface BETWEEN :minSurface AND :maxSurface")
    fun loadPropertiesWithSurfaceFilter(minSurface: Float, maxSurface: Float) : Single<List<Property>>

    @Query ("SELECT * FROM Property where surface BETWEEN :minSurface AND (SELECT MAX(surface) FROM Property)")
    fun loadPropertiesWithMinSurfaceFilter(minSurface: Float) : Single<List<Property>>

    @Query ("SELECT * FROM Property where surface BETWEEN 0 AND :maxSurface")
    fun loadPropertiesWithMaxSurfaceFilter(maxSurface: Float) : Single<List<Property>>

    // Beds
    @Query ("SELECT * FROM Property where numberRoom BETWEEN :minRoom AND :maxRoom")
    fun loadPropertiesWithBedroomFilter(minRoom: Int, maxRoom: Int) : Single<List<Property>>

    @Query ("SELECT * FROM Property where numberRoom BETWEEN :minRoom AND (SELECT MAX(numberRoom) FROM Property)")
    fun loadPropertiesWithMinBedroomFilter(minRoom: Int) : Single<List<Property>>

    @Query ("SELECT * FROM Property where numberRoom BETWEEN 0 AND :maxRoom")
    fun loadPropertiesWithMaxBedroomFilter(maxRoom: Int) : Single<List<Property>>

    // Bathrooms
    @Query ("SELECT * FROM Property where numberBathroom BETWEEN :minBathroom AND :maxBathroom")
    fun loadPropertiesWithBathroomFilter(minBathroom: Int, maxBathroom: Int) : Single<List<Property>>

    @Query ("SELECT * FROM Property where numberBathroom BETWEEN :minBathroom AND (SELECT MAX(numberBathroom) FROM Property)")
    fun loadPropertiesWithMinBathroomFilter(minBathroom: Int) : Single<List<Property>>

    @Query ("SELECT * FROM Property where numberBathroom BETWEEN 0 AND :maxBathroom")
    fun loadPropertiesWithMaxBathroomFilter(maxBathroom: Int) : Single<List<Property>>

    // Options
    @Query("SELECT * FROM Property where options IN (:options)")
    fun loadPropertiesWithOptionsFilter(options: List<Option>) : Single<List<Property>>

    // Status
    @Query("SELECT * FROM Property where status IN (:status)")
    fun loadPropertiesWithStatusFilter(status: List<Status>) : Single<List<Property>>

    // Entry date
    @Query ("SELECT * FROM Property where entryDate BETWEEN :minDate AND :maxDate")
    fun loadPropertiesWithEntryDateFilter(minDate: Date, maxDate: Date) : Single<List<Property>>

    @Query ("SELECT * FROM Property where entryDate BETWEEN :minDate AND (SELECT MAX(entryDate) FROM Property)")
    fun loadPropertiesWithMinEntryDateFilter(minDate: Date) : Single<List<Property>>

    @Query ("SELECT * FROM Property where entryDate BETWEEN 0 AND :maxDate")
    fun loadPropertiesWithMaxEntryDateFilter(maxDate: Date) : Single<List<Property>>

    // Sold date
    @Query ("SELECT * FROM Property where soldDate BETWEEN :minDate AND :maxDate")
    fun loadPropertiesWithSoldDateFilter(minDate: Date, maxDate: Date) : Single<List<Property>>

    @Query ("SELECT * FROM Property where soldDate BETWEEN :minDate AND (SELECT MAX(soldDate) FROM Property)")
    fun loadPropertiesWithMinSoldDateFilter(minDate: Date) : Single<List<Property>>

    @Query ("SELECT * FROM Property where soldDate BETWEEN 0 AND :maxDate")
    fun loadPropertiesWithMaxSoldDateFilter(maxDate: Date) : Single<List<Property>>

    //Sort
    @Query("SELECT * FROM Property ORDER BY price ASC")
    fun loadAllPropertiesWithAscPriceSort() : Observable<List<Property>>

    @Query("SELECT * FROM Property ORDER BY price DESC")
    fun loadAllPropertiesWithDescPriceSort() : Observable<List<Property>>

    @Query("SELECT * FROM Property ORDER BY type")
    fun loadAllPropertiesWithTypeSort() : Observable<List<Property>>

    @Query("SELECT * FROM Property ORDER BY status")
    fun loadAllPropertiesWithStatusSort() : Observable<List<Property>>
}