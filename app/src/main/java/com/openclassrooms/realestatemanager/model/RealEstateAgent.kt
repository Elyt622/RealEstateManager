package com.openclassrooms.realestatemanager.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RealEstateAgent(
    @PrimaryKey (autoGenerate = true) var idAgent: Int,
    var lastName: String,
    var firstName: String
    )
