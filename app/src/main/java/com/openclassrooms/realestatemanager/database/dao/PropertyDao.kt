package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.Property
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface PropertyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProperty(property: Property) : Completable

    @Query("SELECT * FROM Property")
    fun getAllProperty() : Observable<List<Property>>

    @Query("SELECT * FROM Property WHERE ref = :ref")
    fun getPropertyWithRef(ref: Int) : Observable<Property>

}