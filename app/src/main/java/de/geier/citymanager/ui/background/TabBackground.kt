package de.geier.citymanager.ui.background

import de.geier.citymanager.ui.CityTab

/**
 * Reines Wertobjekt für dekorative Tab-Hintergründe.
 * Keine Logik, kein Rendering.
 */
data class TabBackground(
    val tab: CityTab,
    val imageUri: String?
)
