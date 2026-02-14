package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Repräsentiert eine eigenständige Stadt.
 *
 * Jede Stadt ist vollständig gekapselt.
 * Alle stadtabhängigen Entities referenzieren diese ID.
 */
@Entity(tableName = "cities")
data class CityEntity(

    /**
     * Globale eindeutige ID (UUID String).
     * Wird auch später als Firestore-Dokument-ID verwendet.
     */
    @PrimaryKey
    val id: String,

    /**
     * Anzeigename der Stadt.
     * Wird u.a. für "Über [Stadtname]" verwendet.
     */
    val name: String,

    /**
     * Vierstelliger Zugangscode für Spielleiter.
     * Kein Hochsicherheitsmerkmal – einfache Validierung.
     */
    val gameMasterCode: String,

    /**
     * Erstellungszeitpunkt (EpochMillis).
     * Dient Sortierung & späterer Sync-Strategie.
     */
    val createdAt: Long
)
