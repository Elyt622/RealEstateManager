package com.openclassrooms.realestatemanager.database

import androidx.room.TypeConverter
import com.openclassrooms.realestatemanager.model.InterestPoint
import java.util.*


class Converters {
    private val separator = ","

    @TypeConverter
    fun getDBValue(model: MutableList<String>?): String =
        if (model == null || model.isEmpty())
            ""
        else
            model.joinToString(separator = separator) { it }

    @TypeConverter
    fun getModelValue(data: String?): MutableList<String> {
        return data?.split(separator)?.toMutableList() ?: mutableListOf()
    }

    @TypeConverter
    fun getDBInterestPoint(model: MutableList<InterestPoint>?): String =
        if (model == null || model.isEmpty())
            ""
        else
            model.joinToString(separator = separator) { it.displayName }

    @TypeConverter
    fun getModelInterestPoint(data: String?): MutableList<InterestPoint> {
        val interestPointsString = data?.split(separator)?.toMutableList()
        lateinit var interestPoints : MutableList<InterestPoint>
        if (interestPointsString != null) {
            for (interestPointString in interestPointsString) {
                interestPoints.add(InterestPoint.valueOf(interestPointString))
            }
        }
        return interestPoints
    }
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}