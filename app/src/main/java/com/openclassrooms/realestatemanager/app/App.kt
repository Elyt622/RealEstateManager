package com.openclassrooms.realestatemanager.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.room.Room
import com.openclassrooms.realestatemanager.database.AppDatabase

class App : Application() {
    companion object {
        lateinit var database: AppDatabase
        const val NOTIFICATION_CHANNEL_ID = "RealEstateManager"
    }

    override fun onCreate() {
        super.onCreate()
        database = Room
            .databaseBuilder(
                this,
                AppDatabase::class.java,
                "RealEstateManager"
            )
            .createFromAsset("database/myapp.db")
            .build()

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val name = "Real Estate Manager"
        val descriptionText = "New property added"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            importance
        ).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}