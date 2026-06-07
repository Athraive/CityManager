package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "factions")
data class FactionEntity(

    @PrimaryKey
    val id: String,

    /**
     * Referenz auf die Stadt.
     */
    val cityId: String,

    val name: String,
    val shortDescription: String = "",
    val description: String?,
    val imageUri: String? = null,
    val playerNotes: String,
    val gameMasterNotes: String,
    val visible: Boolean
)
