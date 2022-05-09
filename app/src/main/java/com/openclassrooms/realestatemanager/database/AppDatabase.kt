package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.model.Property

@Database(entities = [Property::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao() : PropertyDao
}