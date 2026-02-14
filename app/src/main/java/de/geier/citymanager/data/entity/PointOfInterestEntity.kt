package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pois")
data class PointOfInterestEntity(

    @PrimaryKey
    val id: String,

    /**
     * Referenz auf die Stadt, zu der dieser POI gehört.
     */
    val cityId: String,

    val name: String,
    val description: String?,
    val categoryId: String,
    val visible: Boolean,
    val factionId: String?,
    val playerNotes: String,
    val gameMasterNotes: String,

    val mapX: Float? = null,
    val mapY: Float? = null
)
