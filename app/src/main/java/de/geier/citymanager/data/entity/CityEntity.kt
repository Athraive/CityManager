package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Repräsentiert eine eigenständige Stadt.
 *
 * Jede Stadt ist vollständig gekapselt.
 * Alle stadtabhängigen Entities referenzieren diese ID.
 *
 * Ab Version 6:
 * – Hintergrund-Preset
 * – Optionales eigenes Hintergrundbild
 * – Schrift-Preset
 */
@Entity(tableName = "cities")
data class CityEntity(

    /**
     * Globale eindeutige ID (UUID String).
     * Wird später auch als Firestore-Dokument-ID verwendet.
     */
    @PrimaryKey
    val id: String,

    /**
     * Anzeigename der Stadt.
     */
    val name: String,

    /**
     * Vierstelliger Zugangscode für Spielleiter.
     */
    val gameMasterCode: String,

    /**
     * Erstellungszeitpunkt (EpochMillis).
     */
    val createdAt: Long,

    /* =====================================================
     * THEME (neu ab Version 6)
     * ===================================================== */

    /**
     * Hintergrund-Preset (String gespeichert, Enum im UI).
     * Beispiele:
     * "WHITE", "MEDIEVAL", "SCIFI", ...
     */
    val backgroundPreset: String = "WHITE",

    /**
     * Optional eigenes Hintergrundbild.
     * Überschreibt Preset, wenn gesetzt.
     */
    val backgroundImageUri: String? = null,

    /**
     * Schrift-Preset.
     */
    val fontPreset: String = "DEFAULT"
)
