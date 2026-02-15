package de.geier.citymanager.ui.background

import de.geier.citymanager.data.entity.CityEntity

object CityBackgroundResolver {

    /**
     * Gibt den Hintergrundwert zurück, der effektiv verwendet werden soll.
     *
     * Reihenfolge:
     * 1. Tab-Hintergrund (falls gesetzt)
     * 2. Stadt-Hintergrund
     * 3. null
     */
    fun resolve(
        city: CityEntity?,
        tabBackground: String? = null
    ): String? {

        if (!tabBackground.isNullOrBlank()) {
            return tabBackground
        }

        if (city?.backgroundValue != null) {
            return city.backgroundValue
        }

        return null
    }
}
