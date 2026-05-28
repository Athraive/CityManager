package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class PersonEntity(

    @PrimaryKey
    val id: String,

    /**
     * Referenz auf die Stadt, zu der diese Person gehört.
     */
    val cityId: String,

    val name: String,
    val description: String,

    val shortDescription: String = "",
    val portraitImageUri: String? = null,
    val visible: Boolean = true,
    val playerNotes: String = "",
    val gameMasterNotes: String = "",

    // Kartenkoordinaten (optional, normalisiert 0f–1f)
    val mapX: Float? = null,
    val mapY: Float? = null
)
