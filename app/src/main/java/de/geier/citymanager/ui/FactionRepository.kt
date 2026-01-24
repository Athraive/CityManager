package de.geier.citymanager.ui

import kotlinx.coroutines.flow.StateFlow

/**
 * Repository-Interface für Fraktionen.
 *
 * Definiert die einzige Zugriffsschicht auf Fraktionsdaten.
 */
interface FactionRepository {

    val factions: StateFlow<List<Faction>>

    fun save(faction: Faction)

    fun delete(factionId: String)

    fun getById(factionId: String): Faction?
}
