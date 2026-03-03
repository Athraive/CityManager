package de.geier.citymanager.ui.map

import de.geier.citymanager.ui.Person
import de.geier.citymanager.ui.PointOfInterest

data class PersonWithFactions(
    val person: Person,
    val factionIds: List<String>
)

data class PoiWithFactions(
    val poi: PointOfInterest,
    val factionIds: List<String>
)