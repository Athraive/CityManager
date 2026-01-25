package de.geier.citymanager.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room-Entity für POI-Kategorien.
 *
 * Kategorien strukturieren Points of Interest innerhalb einer Stadt.
 */
@Entity(tableName = "poi_categories")
data class PoiCategoryEntity(

    /**
     * Eindeutige ID der Kategorie.
     */
    @PrimaryKey
    val id: String,

    /**
     * Anzeigename der Kategorie.
     */
    val title: String,

    /**
     * Emoji oder Icon-String.
     */
    val icon: String,

    /**
     * Optionale Beschreibung der Kategorie.
     * Wird Spielern angezeigt, sofern sichtbar.
     */
    val description: String? = null,

    /**
     * Optionales Hintergrundbild (URI).
     */
    val backgroundImageUri: String?,

    /**
     * Sichtbarkeit für Spieler.
     * SL sieht Kategorien immer.
     */
    val visible: Boolean = true
)
