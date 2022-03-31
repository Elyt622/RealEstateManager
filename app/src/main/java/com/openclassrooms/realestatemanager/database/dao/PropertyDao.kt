package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.model.Property

@Dao
interface PropertyDao {
    @Insert
    fun insertProperty(property: Property) : Long

}