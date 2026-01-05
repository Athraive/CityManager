package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pois")
data class PointOfInterestEntity(
    @PrimaryKey
    val id: String,
    val categoryId: String,
    val name: String,
    val description: String,
    val type: String,
    val visible: Boolean
)
