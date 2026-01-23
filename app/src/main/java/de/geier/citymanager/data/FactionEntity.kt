package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persistenz-Entity für Fraktionen.
 *
 * Fraktionen sind eigenständige Domänenobjekte und
 * werden ausschließlich über Beziehungen (CrossRefs)
 * mit Personen und POIs verknüpft.
 */
@Entity(tableName = "factions")
data class FactionEntity(

    @PrimaryKey
    val id: String,

    val name: String,

    /**
     * Öffentliche Beschreibung der Fraktion.
     */
    val description: String,

    /**
     * Gibt an, ob diese Fraktion für Spieler sichtbar ist.
     */
    val visible: Boolean
)
