package de.geier.citymanager.ui

/**
 * Kategorie für Points of Interest.
 */
data class PoiCategory(
    val id: String,
    val title: String,
    val icon: String,

    /**
     * Optionale Beschreibung der Kategorie.
     */
    val description: String? = null,

    val backgroundImageUri: String? = null,

    /**
     * Sichtbarkeit für Spieler.
     * SL sieht Kategorien immer.
     */
    val visible: Boolean = true
)
