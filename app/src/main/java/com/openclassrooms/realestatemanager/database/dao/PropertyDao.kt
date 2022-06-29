package com.openclassrooms.realestatemanager.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

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
    @RawQuery
    fun filter(query: SupportSQLiteQuery) : Single<List<Property>>

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