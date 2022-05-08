package com.openclassrooms.realestatemanager.database

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter
import com.openclassrooms.realestatemanager.model.Option
import java.util.*


class Converters {
    private val separator = ","

    @TypeConverter
    fun getDBValue(model: MutableList<Uri?>): String =
        if (model.isEmpty())
            ""
        else
            model.joinToString(separator = separator) { it.toString() }

    @TypeConverter
    fun getModelValue(data: String): MutableList<Uri> {
        val photosStringList = data.split(separator).toMutableList()
        val photoUriList = mutableListOf<Uri>()

        for (photoString in photosStringList){
            if (photoString.isNotEmpty())
                photoUriList.add(photoString.toUri())
        }
        return photoUriList
    }

    @TypeConverter
    fun getDBOption(model: MutableList<Option>?): String =
        if (model == null || model.isEmpty())
            ""
        else
            model.joinToString(separator = separator) { it.name }


    @TypeConverter
    fun getModelOption(data: String?): MutableList<Option> {
        val optionsString = data?.split(separator)?.toMutableList()
        val options : MutableList<Option> = mutableListOf()

        if (optionsString != null) {
            for (optionString in optionsString) {
                if (optionString.isNotEmpty())
                    options.add(Option.valueOf(optionString))
            }
        }
        return options
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