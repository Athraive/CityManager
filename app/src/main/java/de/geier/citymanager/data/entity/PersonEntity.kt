package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room-Entity für eine Person (NPC) innerhalb einer Stadt.
 *
 * Personen können optional einer Fraktion zugeordnet sein
 * und besitzen einen gemeinsamen Notizbereich.
 */
@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey
    val id: String,

    val name: String,

    val description: String?,

    val portraitImageUri: String?,

    val visible: Boolean,

    /**
     * Optionale Zuordnung zu einer Fraktion.
     * null → keine Fraktion
     */
    val factionId: String?,

    /**
     * Gemeinsamer Notizbereich für SL und Spieler.
     */
    val sharedNotes: String
)
