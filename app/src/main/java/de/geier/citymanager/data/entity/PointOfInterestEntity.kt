package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pois") // ← WICHTIG: wieder konsistent zur DAO
data class PointOfInterestEntity(
    @PrimaryKey
    val id: String,

    val name: String,
    val description: String?,
    val categoryId: String,
    val visible: Boolean,
    val factionId: String?,
    val playerNotes: String,
    val gameMasterNotes: String,

    // Kartenkoordinaten (optional, normalisiert)
    val mapX: Float? = null,
    val mapY: Float? = null
)
