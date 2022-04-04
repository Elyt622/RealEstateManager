package com.openclassrooms.realestatemanager.model

import androidx.room.*
import androidx.room.ForeignKey.NO_ACTION
import com.google.android.gms.maps.model.LatLng
import java.util.*

@Entity
data class Property(
    @PrimaryKey (autoGenerate = true) val ref: Int,
    val type: Type,
    var price: Int,
    val surface: Float?,
    var numberPiece: Int,
    var numberBeds: Int,
    var numberBathroom: Int,
    var description: String,
    var photos: MutableList<String>,
    val address: String,
    var interestPoint: MutableList<InterestPoint>?,
    var state: State,
    var entryDate: Date,
    var sellingDate: Date?,
    var agentName: String,
    var latitude: Double?,
    var longitude: Double?
    )