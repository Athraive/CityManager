package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room-Entity für Personen.
 *
 * Keine Fraktionsspalte mehr – M:N ausschließlich über CrossRef.
 */
@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val portraitImageUri: String? = null,
    val visible: Boolean = true,
    val playerNotes: String = "",
    val gameMasterNotes: String = ""
)
