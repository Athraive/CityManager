package de.geier.citymanager.ui.background

import de.geier.citymanager.ui.CityTab

/**
 * Zentrale Quelle der Wahrheit für Tab-Hintergründe.
 * Absichtlich minimal – Persistenz & Edit kommen später.
 */
class TabBackgroundProvider {

    private val backgrounds: Map<CityTab, TabBackground> = mapOf(
        CityTab.CITY to TabBackground(CityTab.CITY, null),
        CityTab.PERSONS to TabBackground(CityTab.PERSONS, null),
        CityTab.POIS to TabBackground(CityTab.POIS, null),
        CityTab.FACTIONS to TabBackground(CityTab.FACTIONS, null)
    )

    fun backgroundFor(tab: CityTab): TabBackground =
        backgrounds[tab] ?: TabBackground(tab, null)
}
