package com.openclassrooms.realestatemanager.model

import androidx.room.*
import java.util.*

@Entity
data class Property(
    @PrimaryKey (autoGenerate = true) val ref: Int = 0,
    var type: Type = Type.APARTMENT,
    var price: Int = -1,
    var surface: Float? = null,
    var numberRoom: Int = -1,
    var numberBed: Int = -1,
    var numberBathroom: Int = -1,
    var description: String = "",
    var photos: MutableList<String> = mutableListOf(""),
    var address: String = "",
    var options: MutableList<Option>? = mutableListOf(),
    var status: Status = Status.ON_SALE,
    var entryDate: Date = Date(0),
    var soldDate: Date? = null,
    var agentName: String = "",
    var latitude: Double? = null,
    var longitude: Double? = null
    )

