package com.openclassrooms.realestatemanager.database.dao

import androidx.room.*
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface PropertyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProperty(property: Property) : Completable

    @Query("SELECT * FROM Property")
    fun getAllProperty() : Observable<List<Property>>

    @Query("SELECT * FROM Property WHERE ref = :ref")
    fun getPropertyWithRef(ref: Int) : Observable<Property>

    @Update
    fun updatePropertyWithRef(property: Property) : Single<Int>

}