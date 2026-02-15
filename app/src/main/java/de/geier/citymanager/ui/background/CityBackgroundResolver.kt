package de.geier.citymanager.ui.background

import de.geier.citymanager.data.entity.CityEntity

object CityBackgroundResolver {

    /**
     * Gibt den Hintergrund zurück, der effektiv verwendet werden soll.
     *
     * Reihenfolge:
     * 1. Tab-Hintergrund (falls gesetzt)
     * 2. Stadt-eigenes Hintergrundbild (backgroundImageUri)
     * 3. null (Preset wird später separat behandelt)
     */
    fun resolve(
        city: CityEntity?,
        tabBackground: String? = null
    ): String? {

        if (!tabBackground.isNullOrBlank()) {
            return tabBackground
        }

        if (!city?.backgroundImageUri.isNullOrBlank()) {
            return city?.backgroundImageUri
        }

        return null
    }
}
