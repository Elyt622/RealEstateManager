package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.app.App
import com.openclassrooms.realestatemanager.model.Property

class PropertyContentProvider : ContentProvider() {

    private val authority = "com.openclassrooms.realestatemanager.provider"

    private val tableName: String = Property::class.java.simpleName

    val uriProperty: Uri = Uri.parse("content://$authority/$tableName")

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        if (context != null) {
            val cursor: Cursor = App.database.propertyDao().loadAllPropertyWithCursor()
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.property/$authority.$tableName"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, s: String?, strings: Array<out String>?): Int {
        return 0
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<out String>?): Int {
        return 0
    }
}