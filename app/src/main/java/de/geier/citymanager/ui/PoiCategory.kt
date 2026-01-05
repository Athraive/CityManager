package de.geier.citymanager.ui

/**
 * Kategorie für Points of Interest
 */
data class PoiCategory(
    val id: String,
    val title: String,
    val icon: String,
    val backgroundImageUri: String? = null,
    val visible: Boolean = true
)
