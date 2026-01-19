package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persistenz-Entity für Personen.
 *
 * Spiegelt das UI-Modell [Person] wider, enthält aber ausschließlich
 * datenbankrelevante Felder.
 */
@Entity(tableName = "persons")
data class PersonEntity(

    @PrimaryKey
    val id: String,

    val name: String,

    /**
     * Öffentliche Beschreibung der Person.
     *
     * - für Spieler sichtbar
     * - vom SL editierbar
     */
    val description: String,

    val portraitImageUri: String?,

    val visible: Boolean,

    val factionId: String?,

    /**
     * Spieler-Notizen zur Person.
     */
    val playerNotes: String,

    /**
     * Interne Spielleiter-Notizen zur Person.
     */
    val gameMasterNotes: String
)
