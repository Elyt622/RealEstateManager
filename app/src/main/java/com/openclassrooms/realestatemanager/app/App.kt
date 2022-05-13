package com.openclassrooms.realestatemanager.app

import android.app.Application
import androidx.room.Room
import com.openclassrooms.realestatemanager.database.AppDatabase

class App : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "RealEstateManager").createFromAsset("database/myapp.db").build()
    }
}