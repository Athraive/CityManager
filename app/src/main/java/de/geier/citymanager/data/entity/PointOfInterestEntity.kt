package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persistenz-Entity für Points of Interest (POIs).
 *
 * Spiegelt das Domain-Modell [PointOfInterest] wider.
 * Enthält ausschließlich datenbankrelevante Felder.
 */
@Entity(tableName = "pois")
data class PointOfInterestEntity(

    @PrimaryKey
    val id: String,

    /**
     * Kategorie-ID, zu der dieser POI gehört.
     */
    val categoryId: String,

    /**
     * Anzeigename des POI.
     */
    val name: String,

    /**
     * Öffentliche Beschreibung des POI.
     */
    val description: String,

    /**
     * Typ des POI (serialisiert).
     */
    val type: String,

    /**
     * Gibt an, ob der POI für Spieler sichtbar ist.
     */
    val visible: Boolean,

    /**
     * Optionale Zuordnung zu einer Fraktion.
     */
    val factionId: String?,

    /**
     * Spieler-Notizen zum POI.
     */
    val playerNotes: String,

    /**
     * Interne Spielleiter-Notizen zum POI.
     */
    val gameMasterNotes: String
)
