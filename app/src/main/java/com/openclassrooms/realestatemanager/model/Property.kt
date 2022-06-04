package com.openclassrooms.realestatemanager.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Property(
    @PrimaryKey (autoGenerate = true) var ref: Int = 0,
    var type: Type = Type.APARTMENT,
    var price: Int = -1,
    var surface: Float? = null,
    var numberRoom: Int = -1,
    var numberBed: Int = -1,
    var numberBathroom: Int = -1,
    var description: String = "",
    var photos: MutableList<Uri> = mutableListOf(),
    var address: String = "",
    var options: MutableList<Option>? = mutableListOf(),
    var status: Status = Status.ON_SALE,
    var entryDate: Date = Date(),
    var soldDate: Date? = null,
    var agentName: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
    )

