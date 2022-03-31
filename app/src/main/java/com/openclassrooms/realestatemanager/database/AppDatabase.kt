package com.openclassrooms.realestatemanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.database.dao.PropertyDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateAgentDao
import com.openclassrooms.realestatemanager.model.Property
import com.openclassrooms.realestatemanager.model.RealEstateAgent

@Database(entities = [Property::class, RealEstateAgent::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao() : PropertyDao
    abstract fun realEstateManagerDao(): RealEstateAgentDao
}