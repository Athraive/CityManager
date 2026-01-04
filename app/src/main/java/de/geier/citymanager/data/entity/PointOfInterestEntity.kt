package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "points_of_interest")
data class PointOfInterestEntity(
    @PrimaryKey
    val id: String,

    val name: String,
    val description: String,
    val categoryId: String,
    val type: String,      // PoiType als String
    val visible: Boolean
)
