package com.openclassrooms.realestatemanager.provider.provider

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.provider.PropertyContentProvider
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PropertyContentProviderTests {

    private var contentResolver: ContentResolver? = null

    private val userId: Long = 1

    @Before
    fun setup() {
        Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        contentResolver = InstrumentationRegistry.getInstrumentation().context
            .contentResolver
    }

    @Test
    fun getAllProperties() {
        val cursor: Cursor? = contentResolver!!.query(
            ContentUris.withAppendedId(
                PropertyContentProvider().uriProperty,
                userId
            ), null, null, null, null
        )
        assertThat(cursor, notNullValue())
        cursor?.close()
    }
}