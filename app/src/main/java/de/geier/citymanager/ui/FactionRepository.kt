package de.geier.citymanager.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-Memory Repository für Fraktionen.
 *
 * Enthält initiale Seed-Daten für Entwicklungszwecke.
 * KEINE Persistenz.
 */
class FactionRepository {

    /**
     * Interner, veränderbarer State.
     *
     * Beim Start mit Demo-Fraktionen befüllt.
     */
    private val _factions = MutableStateFlow(
        listOf(
            Faction(
                id = "faction_guard",
                name = "Stadtwache",
                description = "Verantwortlich für Ordnung und Sicherheit.",
                visible = true
            ),
            Faction(
                id = "faction_merchants",
                name = "Händlergilde",
                description = "Vereinigung der Kaufleute und Handwerker.",
                visible = true
            ),
            Faction(
                id = "faction_shadows",
                name = "Die Schatten",
                description = "Geheimbund mit unbekannten Zielen.",
                visible = false
            )
        )
    )

    /**
     * Öffentlicher, nur lesbarer Zugriff.
     */
    val factions: StateFlow<List<Faction>> = _factions.asStateFlow()

    /**
     * Fügt eine neue Fraktion hinzu oder ersetzt sie,
     * falls die ID bereits existiert.
     */
    fun save(faction: Faction) {
        val current = _factions.value.toMutableList()
        val index = current.indexOfFirst { it.id == faction.id }

        if (index >= 0) {
            current[index] = faction
        } else {
            current.add(faction)
        }

        _factions.value = current
    }

    /**
     * Entfernt eine Fraktion anhand ihrer ID.
     */
    fun delete(factionId: String) {
        _factions.value = _factions.value.filterNot { it.id == factionId }
    }

    /**
     * Liefert eine Fraktion anhand ihrer ID oder null,
     * falls sie nicht existiert.
     */
    fun getById(factionId: String): Faction? {
        return _factions.value.firstOrNull { it.id == factionId }
    }
}
