package com.example.ubapetdata.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sightings")
data class Sighting(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val animalType: AnimalType,
    val condition: AnimalCondition,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val status: SightingStatus = SightingStatus.OPEN
)
