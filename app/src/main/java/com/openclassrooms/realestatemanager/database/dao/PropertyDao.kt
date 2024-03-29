package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
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

    @Query("SELECT * FROM Property")
    fun loadAllPropertyWithCursor() : Cursor

    @Query("SELECT * FROM Property WHERE ref = :ref")
    fun loadPropertyWithRef(ref: Int) : Observable<Property>

    @Update
    fun updateProperty(property: Property) : Completable

    @Query("SELECT DISTINCT addressArea FROM Property")
    fun loadAllAddressArea() : Single<List<String>>

    // Filter
    @RawQuery
    fun filter(query: SupportSQLiteQuery) : Single<List<Property>>
}