package com.openclassrooms.realestatemanager.model

import androidx.room.*
import java.util.*

@Entity
data class Property(
    @PrimaryKey (autoGenerate = true) val ref: Int,
    val type: Type,
    var price: Int,
    val surface: Float?,
    var numberRoom: Int,
    var numberBed: Int,
    var numberBathroom: Int,
    var description: String,
    var photos: MutableList<String>,
    val address: String,
    var options: MutableList<Option>?,
    var status: Status,
    var entryDate: Date,
    var soldDate: Date?,
    var agentName: String,
    var latitude: Double?,
    var longitude: Double?
    )